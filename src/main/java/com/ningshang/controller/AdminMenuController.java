package com.ningshang.controller;

import com.ningshang.dto.ApiResponse;
import javax.validation.Valid;
import com.ningshang.exception.BusinessException;
import com.ningshang.entity.AdminMenu;
import com.ningshang.service.AdminGroupService;
import com.ningshang.service.AdminMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 后台菜单管理（仅超级管理员组可见）
 */
@RestController
@RequestMapping("/api/admin/menus")
public class AdminMenuController {

    @Autowired
    private AdminMenuService menuService;
    @Autowired
    private AdminGroupService groupService;

    /** 完整菜单树（管理页面用） */
    @GetMapping
    public ApiResponse<List<AdminMenu>> tree() {
        return ApiResponse.success(menuService.tree());
    }

    /** 当前管理员可见的菜单树（侧边栏用） */
    @GetMapping("/mine")
    public ApiResponse<List<AdminMenu>> mine(HttpServletRequest request) {
        String username = (String) request.getAttribute("adminUsername");
        if (username == null) {
            return ApiResponse.error(401, "未登录");
        }
        return ApiResponse.success(groupService.getMenusForAdmin(username));
    }

    @PostMapping
    public ApiResponse<AdminMenu> create(@Valid @RequestBody AdminMenu menu) {
        return ApiResponse.success(menuService.save(menu));
    }

    @PutMapping("/{id}")
    public ApiResponse<AdminMenu> update(@PathVariable Long id, @Valid @RequestBody AdminMenu menu) {
        menu.setId(id);
        return ApiResponse.success(menuService.save(menu));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(@PathVariable Long id) {
        menuService.delete(id);
        return ApiResponse.success("删除成功");
    }

    /** 批量删除 */
    @PostMapping("/batch-delete")
    public ApiResponse<String> batchDelete(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException(400, "请选择要删除的菜单");
        }
        menuService.batchDelete(ids);
        return ApiResponse.success("批量删除成功");
    }

    /** 所有菜单平铺列表（权限配置用） */
    @GetMapping("/flat")
    public ApiResponse<List<AdminMenu>> flat() {
        return ApiResponse.success(menuService.listAll());
    }
}
