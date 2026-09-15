package com.ningshang.controller;
import com.ningshang.dto.ApiResponse;
import com.ningshang.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired private AdminService adminService;
    @Autowired private com.ningshang.util.JwtUtil jwtUtil;
    @PostMapping("/login")
    public ApiResponse<Map<String,Object>> login(@RequestBody Map<String,String> params) {
        return ApiResponse.success(adminService.login(params.get("username"), params.get("password")));
    }
    @PostMapping("/logout")
    public ApiResponse<String> logout(@RequestHeader("Authorization") String authorization) {
        jwtUtil.revokeToken(authorization.substring(7));
        return ApiResponse.success("退出成功");
    }
}
