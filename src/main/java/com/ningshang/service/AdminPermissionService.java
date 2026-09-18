package com.ningshang.service;

import com.ningshang.entity.*;
import com.ningshang.exception.BusinessException;
import com.ningshang.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminPermissionService {
    @Autowired private AdminPermissionRepository permissionRepository;
    @Autowired private AdminGroupPermissionRepository groupPermissionRepository;
    @Autowired private AdminRepository adminRepository;
    @Autowired private AdminGroupRepository groupRepository;
    @PersistenceContext private EntityManager entityManager;

    public List<AdminPermission> listAll() { return permissionRepository.findAllByOrderBySortOrderAscIdAsc(); }
    public List<Long> getIds(Long groupId) { return groupPermissionRepository.findByGroupId(groupId).stream().map(AdminGroupPermission::getPermissionId).collect(Collectors.toList()); }
    public List<String> getCodes(String username) {
        Admin admin = adminRepository.findByUsername(username).orElseThrow(() -> new BusinessException(401, "账号不存在"));
        if (Long.valueOf(1).equals(admin.getGroupId())) return listAll().stream().filter(p -> Integer.valueOf(1).equals(p.getStatus())).map(AdminPermission::getCode).collect(Collectors.toList());
        Set<Long> ids = new HashSet<>(getIds(admin.getGroupId()));
        return listAll().stream().filter(p -> ids.contains(p.getId()) && Integer.valueOf(1).equals(p.getStatus())).map(AdminPermission::getCode).collect(Collectors.toList());
    }
    public boolean has(String username, String code) { return getCodes(username).contains(code); }
    @Transactional public void update(Long groupId, List<Long> ids) {
        if (!groupRepository.existsById(groupId)) throw new BusinessException(404, "权限组不存在");
        if (ids == null || ids.contains(null)) throw new BusinessException(400, "权限列表不正确");
        Set<Long> uniqueIds = new LinkedHashSet<>(ids);
        if (permissionRepository.findAllById(uniqueIds).size() != uniqueIds.size()) throw new BusinessException(400, "操作权限不存在");
        entityManager.createNativeQuery("DELETE FROM admin_group_permission WHERE group_id = :gid")
            .setParameter("gid", groupId).executeUpdate();
        entityManager.flush();
        entityManager.clear();
        if (uniqueIds.isEmpty()) return;
        List<AdminGroupPermission> relations = uniqueIds.stream()
            .map(id -> { AdminGroupPermission gp = new AdminGroupPermission(); gp.setGroupId(groupId); gp.setPermissionId(id); return gp; })
            .collect(Collectors.toList());
        groupPermissionRepository.saveAll(relations);
    }
}
