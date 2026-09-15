package com.ningshang.service;

import com.ningshang.entity.*;
import com.ningshang.exception.BusinessException;
import com.ningshang.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminPermissionService {
    @Autowired private AdminPermissionRepository permissionRepository;
    @Autowired private AdminGroupPermissionRepository groupPermissionRepository;
    @Autowired private AdminRepository adminRepository;
    @Autowired private AdminGroupRepository groupRepository;

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
        groupPermissionRepository.deleteByGroupId(groupId);
        if (ids == null) return;
        Set<Long> valid = permissionRepository.findAllById(ids).stream().map(AdminPermission::getId).collect(Collectors.toSet());
        for (Long id : new LinkedHashSet<>(ids)) if (valid.contains(id)) { AdminGroupPermission gp = new AdminGroupPermission(); gp.setGroupId(groupId); gp.setPermissionId(id); groupPermissionRepository.save(gp); }
    }
}
