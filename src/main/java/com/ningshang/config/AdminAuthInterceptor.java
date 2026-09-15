package com.ningshang.config;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ningshang.dto.ApiResponse;
import com.ningshang.entity.Admin;
import com.ningshang.entity.AdminGroup;
import com.ningshang.repository.AdminRepository;
import com.ningshang.repository.AdminGroupRepository;
import com.ningshang.service.AdminGroupService;
import com.ningshang.service.AdminPermissionService;
import com.ningshang.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AdminAuthInterceptor implements HandlerInterceptor {
    @Autowired private JwtUtil jwtUtil;
    @Autowired private AdminGroupService groupService;
    @Autowired private AdminRepository adminRepository;
    @Autowired private AdminGroupRepository groupRepository;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private AdminPermissionService permissionService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) return true;
        String uri = request.getRequestURI().substring(request.getContextPath().length());
        if ("/api/admin/login".equals(uri)) return true;
        String auth = request.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ") || !jwtUtil.validateToken(auth.substring(7))) {
            return deny(response, 401, "未登录或登录已过期");
        }
        String username = jwtUtil.getUsername(auth.substring(7));
        Admin admin = adminRepository.findByUsername(username).orElse(null);
        if (admin == null) return deny(response, 401, "账号已不存在，请重新登录");
        AdminGroup group = admin.getGroupId() == null ? null : groupRepository.findById(admin.getGroupId()).orElse(null);
        if (group == null || !Integer.valueOf(1).equals(group.getStatus())) return deny(response, 403, "权限组不存在或已禁用");
        request.setAttribute("adminUsername", username);
        if (uri.equals("/api/admin/upload") || uri.equals("/api/admin/password")
            || uri.equals("/api/admin/menus/mine") || uri.equals("/api/admin/permissions/mine") || uri.equals("/api/admin/logout")) return true;
        // 系统权限取自数据库账号所属组，不能由可编辑的菜单路径推导。
        if (Long.valueOf(1).equals(admin.getGroupId())) return true;
        String permission = resolvePermission(uri, request.getMethod());
        if (permission != null && !permissionService.has(username, permission)) return deny(response, 403, "缺少操作权限：" + permission);
        String normalizedUri = uri.substring(4);
        if (normalizedUri.startsWith("/admin/permissions")) normalizedUri = "/admin/groups";
        for (String path : groupService.getAllowedPaths(username)) {
            String normalized = path.startsWith("/ningshang-admin/")
                ? "/admin/" + path.substring("/ningshang-admin/".length()) : path;
            if ("/admin/site-settings".equals(normalized)) normalized = "/admin/content";
            if (normalized.startsWith("/admin/content/")) normalized = "/admin/content";
            if (normalized.startsWith("/admin/") && matches(normalizedUri, normalized)) return true;
        }
        return deny(response, 403, "无权访问该功能");
    }
    private String resolvePermission(String uri, String method) {
        if (!uri.startsWith("/api/admin/")) return null;
        String rest = uri.substring("/api/admin/".length());
        String module = rest.split("/")[0];
        if (module.equals("permissions")) return "groups:list";
        if (module.equals("site-settings")) module = "content";
        if (rest.endsWith("/batch-delete")) return module + ":batch_delete";
        String action = "GET".equals(method) ? "list" : "POST".equals(method) ? "create" : "PUT".equals(method) ? "update" : "DELETE".equals(method) ? "delete" : null;
        return action == null ? null : module + ":" + action;
    }
    private boolean matches(String uri, String path) { return uri.equals(path) || uri.startsWith(path + "/"); }
    private boolean deny(HttpServletResponse response, int status, String message) throws Exception {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(ApiResponse.error(status, message)));
        return false;
    }
}
