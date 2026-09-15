package com.ningshang.controller;

import com.ningshang.dto.ApiResponse;
import javax.validation.Valid;
import com.ningshang.exception.BusinessException;
import com.ningshang.entity.AdminGroup;
import com.ningshang.service.AdminGroupService;
import com.ningshang.service.AdminPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 权限组管理（仅超级管理员组可见）
 */
@RestController
@RequestMapping("/api/admin/groups")
public class AdminGroupController {

    @Autowired
    private AdminGroupService groupService;
    @Autowired private AdminPermissionService permissionService;

    @GetMapping
    public ApiResponse<List<AdminGroup>> list() {
        return ApiResponse.success(groupService.findAll());
    }

    @PostMapping
    public ApiResponse<AdminGroup> create(@Valid @RequestBody AdminGroup group) {
        return ApiResponse.success(groupService.save(group));

    }

    @PutMapping("/{id}")
    public ApiResponse<AdminGroup> update(@PathVariable Long id, @Valid @RequestBody AdminGroup group) {
        group.setId(id);
        return ApiResponse.success(groupService.save(group));

    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(@PathVariable Long id) {
        groupService.delete(id);
        return ApiResponse.success("删除成功");

    }

    @PostMapping("/batch-delete")
    public ApiResponse<String> batchDelete(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty() || ids.contains(null)) throw new BusinessException(400, "请选择要删除的角色");
        java.util.Set<Long> uniqueIds = new java.util.LinkedHashSet<>(ids);
        for (Long id : uniqueIds) groupService.delete(id);
        return ApiResponse.success("成功删除 " + uniqueIds.size() + " 个角色");
    }

    /** 获取组的菜单权限 */
    @GetMapping("/{id}/menus")
    public ApiResponse<List<Long>> getMenus(@PathVariable Long id) {
        return ApiResponse.success(groupService.getMenuIds(id));
    }

    /** 更新组的菜单权限 */
    @PutMapping("/{id}/menus")
    public ApiResponse<String> updateMenus(@PathVariable Long id, @RequestBody List<Long> menuIds) {
        groupService.updateGroupMenus(id, menuIds);
        return ApiResponse.success("权限更新成功");
    }

    @GetMapping("/{id}/permissions")
    public ApiResponse<List<Long>> getPermissions(@PathVariable Long id) { return ApiResponse.success(permissionService.getIds(id)); }

    @PutMapping("/{id}/permissions")
    public ApiResponse<String> updatePermissions(@PathVariable Long id, @RequestBody List<Long> ids) {
        permissionService.update(id, ids);
        return ApiResponse.success("操作权限更新成功");
    }
}
