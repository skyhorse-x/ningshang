package com.ningshang.controller;
import com.ningshang.dto.ApiResponse;
import com.ningshang.entity.AdminPermission;
import com.ningshang.service.AdminPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
@RestController @RequestMapping("/api/admin/permissions")
public class AdminPermissionController {
    @Autowired private AdminPermissionService service;
    @GetMapping public ApiResponse<List<AdminPermission>> list() { return ApiResponse.success(service.listAll()); }
    @GetMapping("/mine") public ApiResponse<List<String>> mine(HttpServletRequest request) { return ApiResponse.success(service.getCodes((String) request.getAttribute("adminUsername"))); }
}
