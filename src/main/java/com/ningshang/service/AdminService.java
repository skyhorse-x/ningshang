package com.ningshang.service;

import com.ningshang.exception.BusinessException;
import com.ningshang.entity.Admin;
import com.ningshang.repository.AdminGroupRepository;
import com.ningshang.repository.AdminRepository;
import com.ningshang.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /** 初始管理员密码（仅首次创建时使用，环境变量 ADMIN_DEFAULT_PASSWORD 可覆盖） */
    @Value("${admin.default-password:Admin@2026}")
    private String defaultPassword;

    // ==================== 登录限流（内存实现，单机适用） ====================
    private static final int MAX_ATTEMPTS = 5;
    private static final long LOCK_MILLIS = 15 * 60 * 1000L;

    private static class FailRecord {
        int count;
        long lockUntil;
        long windowStart;
    }

    private final ConcurrentHashMap<String, FailRecord> failRecords = new ConcurrentHashMap<>();

    public Map<String, Object> login(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.isEmpty()) {
            throw new BusinessException(401, "用户名或密码错误");
        }
        username = username.trim();

        // 登录频率限制：连续失败 MAX_ATTEMPTS 次后锁定 LOCK_MILLIS
        FailRecord record = failRecords.get(username);
        long now = System.currentTimeMillis();
        if (record != null && record.lockUntil > now) {
            long remainSec = (record.lockUntil - now) / 1000;
            throw new BusinessException(429, "失败次数过多，账号已临时锁定，请 " + Math.max(remainSec / 60 + 1, 1) + " 分钟后重试");
        }

        Optional<Admin> adminOpt = adminRepository.findByUsername(username);
        // 统一错误提示，避免泄露账号是否存在
        String commonError = "用户名或密码错误";
        if (adminOpt.isEmpty()) {
            recordFailure(username);
            throw new BusinessException(401, commonError);
        }
        Admin admin = adminOpt.get();
        String stored = admin.getPassword();
        boolean matched;
        if (stored != null && stored.startsWith("$2")) {
            // BCrypt 密文
            matched = passwordEncoder.matches(password, stored);
        } else {
            // 存量明文密码：明文比对，登录成功后自动迁移为 BCrypt
            matched = stored != null && stored.equals(password);
            if (matched) {
                admin.setPassword(passwordEncoder.encode(password));
                adminRepository.save(admin);
            }
        }
        if (!matched) {
            recordFailure(username);
            throw new BusinessException(401, commonError);
        }

        com.ningshang.entity.AdminGroup group = admin.getGroupId() == null ? null : adminGroupRepository.findById(admin.getGroupId()).orElse(null);
        if (group == null || !Integer.valueOf(1).equals(group.getStatus())) throw new BusinessException(403, "权限组不存在或已禁用");
        failRecords.remove(username);
        String token = jwtUtil.generateToken(admin.getUsername(), admin.getRole(), admin.getGroupId());
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("username", admin.getUsername());
        result.put("nickname", admin.getNickname());
        result.put("role", admin.getRole());
        result.put("groupId", admin.getGroupId());
        return result;
    }

    private void recordFailure(String username) {
        long now = System.currentTimeMillis();
        FailRecord record = failRecords.compute(username, (k, v) -> {
            if (v == null || now - v.windowStart > LOCK_MILLIS) {
                v = new FailRecord();
                v.windowStart = now;
            }
            v.count++;
            if (v.count >= MAX_ATTEMPTS) {
                v.lockUntil = now + LOCK_MILLIS;
            }
            return v;
        });
    }

    /** 强制重置指定管理员密码为默认密码（开发环境便捷） */
    public void resetAdminPassword(String username) {
        adminRepository.findByUsername(username).ifPresent(admin -> {
            admin.setPassword(passwordEncoder.encode(defaultPassword));
            adminRepository.save(admin);
        });
    }

    public void createDefaultAdmin() {
        // 仅首次安装时创建管理员；已有账号绝不重置密码。
        // 存量明文密码在此处静默迁移为 BCrypt（保留原密码，不重置）。
        Optional<Admin> adminOpt = adminRepository.findByUsername("admin");
        if (adminOpt.isEmpty()) {
            Admin admin = new Admin();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode(defaultPassword));
            admin.setNickname("超级管理员");
            admin.setRole("SUPER_ADMIN");
            adminRepository.save(admin);
        } else {
            Admin admin = adminOpt.get();
            String stored = admin.getPassword();
            if (stored == null || !stored.startsWith("$2")) {
                // 明文 → BCrypt 迁移，保留原密码内容
                admin.setPassword(passwordEncoder.encode(stored == null ? "" : stored));
                adminRepository.save(admin);
            }
        }
    }

    public void changePassword(String username, String oldPassword, String newPassword) {
        Optional<Admin> adminOpt = adminRepository.findByUsername(username);
        if (adminOpt.isEmpty()) {
            throw new BusinessException(400, "用户不存在");
        }
        Admin admin = adminOpt.get();
        String stored = admin.getPassword();
        if (oldPassword == null || oldPassword.isEmpty()) throw new BusinessException(400, "原密码不能为空");
        boolean oldMatched = (stored != null && stored.startsWith("$2"))
                ? passwordEncoder.matches(oldPassword, stored)
                : (stored != null && stored.equals(oldPassword));
        if (!oldMatched) {
            throw new BusinessException(400, "原密码错误");
        }
        if (newPassword == null || newPassword.length() < 6) {
            throw new BusinessException(400, "新密码长度不能少于6位");
        }
        admin.setPassword(passwordEncoder.encode(newPassword));
        adminRepository.save(admin);
    }

    // ==================== 管理员账号管理（基于权限组） ====================

    @Autowired
    private AdminGroupRepository adminGroupRepository;

    /** 管理员列表（不返回密码） */
    public List<Map<String, Object>> listAdmins() {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Admin a : adminRepository.findAll()) {
            result.add(toSafeMap(a));
        }
        return result;
    }

    /** 新增管理员（groupId 替代 role） */
    public Map<String, Object> createAdmin(String username, String password, String nickname, Long groupId) {
        if (username == null || username.trim().isEmpty()) {
            throw new BusinessException(400, "用户名不能为空");
        }
        if (adminRepository.findByUsername(username.trim()).isPresent()) {
            throw new BusinessException(400, "用户名已存在");
        }
        validateGroup(groupId);
        if (password == null || password.length() < 6) {
            throw new BusinessException(400, "密码长度不能少于6位");
        }
        Admin admin = new Admin();
        admin.setUsername(username.trim());
        admin.setPassword(passwordEncoder.encode(password));
        admin.setNickname(nickname == null || nickname.trim().isEmpty() ? username.trim() : nickname.trim());
        admin.setGroupId(groupId);
        // 兼容旧字段：同步 role
        admin.setRole(groupId != null && groupId == 1L ? "SUPER_ADMIN" : "ADMIN");
        adminRepository.save(admin);
        return toSafeMap(admin);
    }

    /** 编辑管理员（用户名不可改；密码留空表示不修改） */
    public Map<String, Object> updateAdmin(Long id, String nickname, Long groupId, String password) {
        Admin admin = adminRepository.findById(id).orElseThrow(() -> new BusinessException(400, "管理员不存在"));
        if (groupId != null) {
            if (Long.valueOf(1).equals(admin.getGroupId()) && !Long.valueOf(1).equals(groupId)
                    && adminRepository.countByGroupId(1L) <= 1) {
                throw new BusinessException(400, "系统至少需要保留一个超级管理员");
            }
            validateGroup(groupId);
            admin.setGroupId(groupId);
            admin.setRole(groupId == 1L ? "SUPER_ADMIN" : "ADMIN");
        }
        if (password != null && !password.isEmpty()) {
            if (password.length() < 6) {
                throw new BusinessException(400, "密码长度不能少于6位");
            }
            admin.setPassword(passwordEncoder.encode(password));
        }
        if (nickname != null && !nickname.trim().isEmpty()) {
            admin.setNickname(nickname.trim());
        }
        adminRepository.save(admin);
        return toSafeMap(admin);
    }

    /** 删除管理员（不能删自己；不能删最后一个超级管理员组的成员） */
    public void deleteAdmin(Long id, String currentUsername) {
        Admin admin = adminRepository.findById(id).orElseThrow(() -> new BusinessException(400, "管理员不存在"));
        if (admin.getUsername().equals(currentUsername)) {
            throw new BusinessException(400, "不能删除当前登录的账号");
        }
        // 不能删最后一个超级管理员组成员
        if (admin.getGroupId() != null && admin.getGroupId() == 1L
                && adminRepository.countByGroupId(1L) <= 1) {
            throw new BusinessException(400, "系统至少需要保留一个超级管理员");
        }
        adminRepository.deleteById(id);
    }

    private void validateGroup(Long groupId) {
        if (groupId == null || !adminGroupRepository.existsById(groupId)) {
            throw new BusinessException(400, "无效的权限组");
        }
    }

    private Map<String, Object> toSafeMap(Admin a) {
        Map<String, Object> m = new HashMap<>();
        m.put("id", a.getId());
        m.put("username", a.getUsername());
        m.put("nickname", a.getNickname());
        m.put("role", a.getRole());
        m.put("groupId", a.getGroupId());
        m.put("createdAt", a.getCreatedAt());
        return m;
    }
}
