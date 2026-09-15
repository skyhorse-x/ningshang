package com.ningshang.service;
import com.ningshang.entity.*;
import com.ningshang.repository.*;
import com.ningshang.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminGroupService {
    @Autowired private AdminGroupRepository groupRepository;
    @Autowired private AdminGroupMenuRepository groupMenuRepository;
    @Autowired private AdminRepository adminRepository;
    @Autowired private AdminMenuRepository menuRepository;
    @Autowired private AdminMenuService menuService;
    @Autowired private AdminGroupPermissionRepository groupPermissionRepository;
    public List<AdminGroup> findAll() { return groupRepository.findAll(); }
    public AdminGroup findById(Long id) { return groupRepository.findById(id).orElseThrow(() -> new BusinessException(404, "权限组不存在")); }
    @Transactional
    public AdminGroup save(AdminGroup group) {
        if (group.getId() != null) {
            AdminGroup existing = findById(group.getId());
            if (Long.valueOf(1).equals(group.getId()) && !Integer.valueOf(1).equals(group.getStatus())) throw new BusinessException(400, "不能禁用超级管理员组");
            group.setCreatedAt(existing.getCreatedAt());
        }
        return groupRepository.save(group);
    }
    @Transactional
    public void delete(Long id) {
        findById(id);
        if (Long.valueOf(1).equals(id)) throw new BusinessException(400, "不能删除超级管理员组");
        if (adminRepository.countByGroupId(id) > 0) throw new BusinessException(400, "该权限组下还有管理员账号，无法删除");
        groupMenuRepository.deleteByGroupId(id);
        groupPermissionRepository.deleteByGroupId(id);
        groupRepository.deleteById(id);
    }
    public List<Long> getMenuIds(Long groupId) { findById(groupId); return groupMenuRepository.findMenuIdByGroupId(groupId); }
    @Transactional
    public void updateGroupMenus(Long groupId, List<Long> menuIds) {
        findById(groupId);
        if (menuIds == null || menuIds.contains(null)) throw new BusinessException(400, "菜单列表不正确");
        Set<Long> ids = new LinkedHashSet<>(menuIds);
        if (menuRepository.findAllById(ids).size() != ids.size()) throw new BusinessException(400, "菜单不存在");
        groupMenuRepository.deleteByGroupId(groupId);
        groupMenuRepository.flush();
        List<AdminGroupMenu> relations = ids.stream().map(menuId -> {
            AdminGroupMenu r = new AdminGroupMenu(); r.setGroupId(groupId); r.setMenuId(menuId); return r;
        }).collect(Collectors.toList());
        groupMenuRepository.saveAll(relations);
    }
    public List<AdminMenu> getMenusForAdmin(String username) {
        Admin admin = adminRepository.findByUsername(username).orElse(null);
        if (admin == null || admin.getGroupId() == null) return Collections.emptyList();
        AdminGroup group = groupRepository.findById(admin.getGroupId()).orElse(null);
        if (group == null || !Integer.valueOf(1).equals(group.getStatus())) return Collections.emptyList();
        Set<Long> ids = Long.valueOf(1).equals(admin.getGroupId())
            ? menuRepository.findAll().stream().map(AdminMenu::getId).collect(Collectors.toSet())
            : new HashSet<>(groupMenuRepository.findMenuIdByGroupId(admin.getGroupId()));
        return menuService.treeByMenuIds(ids);
    }
    public Set<String> getAllowedPaths(String username) {
        Admin admin = adminRepository.findByUsername(username).orElse(null);
        if (admin == null || admin.getGroupId() == null) return Collections.emptySet();
        Set<Long> assigned = new HashSet<>(groupMenuRepository.findMenuIdByGroupId(admin.getGroupId()));
        Set<String> paths = new HashSet<>();
        for (AdminMenu root : getMenusForAdmin(username)) {
            if (assigned.contains(root.getId()) && root.getPath() != null && !root.getPath().isEmpty()) paths.add(root.getPath());
            for (AdminMenu child : root.getChildren()) {
                if (assigned.contains(child.getId()) && child.getPath() != null && !child.getPath().isEmpty()) paths.add(child.getPath());
            }
        }
        return paths;
    }
}
