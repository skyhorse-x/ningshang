package com.ningshang.controller;

import com.ningshang.dto.ApiResponse;
import com.ningshang.dto.PageResult;
import javax.validation.Valid;
import com.ningshang.exception.BusinessException;
import com.ningshang.entity.*;
import com.ningshang.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashSet;

/**
 * 管理后台统一 API：除 login/logout 外，全部经过 JWT 鉴权
 */
@RestController
@RequestMapping("/api/admin")
public class AdminApiController {

    @Autowired private NewsService newsService;
    @Autowired private JobService jobService;
    @Autowired private MessageService messageService;
    @Autowired private SubsidiaryService subsidiaryService;
    @Autowired private CoreBusinessService coreBusinessService;
    @Autowired private TeamService teamService;
    @Autowired private HonorService honorService;
    @Autowired private MilestoneService milestoneService;
    @Autowired private SiteContentService siteContentService;
    @Autowired private PartnerService partnerService;
    @Autowired private AdminService adminService;

    /** 业务列表批量删除；逐条调用现有服务以复用不存在校验和保护规则。 */
    @PostMapping("/{module}/batch-delete")
    public ApiResponse<String> batchDelete(@PathVariable String module, @RequestBody List<Long> ids, HttpServletRequest request) {
        if (ids == null || ids.isEmpty() || ids.contains(null)) throw new BusinessException(400, "请选择要删除的数据");
        LinkedHashSet<Long> uniqueIds = new LinkedHashSet<>(ids);
        if (uniqueIds.size() > 200) throw new BusinessException(400, "一次最多删除200条数据");
        for (Long id : uniqueIds) {
            switch (module) {
                case "news": newsService.delete(id); break;
                case "jobs": jobService.delete(id); break;
                case "messages": messageService.delete(id); break;
                case "subsidiaries": subsidiaryService.delete(id); break;
                case "core-businesses": coreBusinessService.delete(id); break;
                case "team": teamService.delete(id); break;
                case "honors": honorService.delete(id); break;
                case "milestones": milestoneService.delete(id); break;
                case "content": siteContentService.delete(id); break;
                case "admins": adminService.deleteAdmin(id, (String) request.getAttribute("adminUsername")); break;
                case "partners": partnerService.delete(id); break;
                default: throw new BusinessException(400, "该模块不支持批量删除");
            }
        }
        return ApiResponse.success("成功删除 " + uniqueIds.size() + " 条数据");
    }

    // ==================== 新闻管理 ====================
    @GetMapping("/news")
    public ApiResponse<PageResult<News>> listNews(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String category) {
        return ApiResponse.success(newsService.findAdminPage(page, size, title, category));
    }

    @PostMapping("/news")
    public ApiResponse<News> createNews(@Valid @RequestBody News news) {
        return ApiResponse.success(newsService.save(news));
    }

    @PutMapping("/news/{id}")
    public ApiResponse<News> updateNews(@PathVariable Long id, @Valid @RequestBody News news) {
        news.setId(id);
        return ApiResponse.success(newsService.save(news));
    }

    @DeleteMapping("/news/{id}")
    public ApiResponse<String> deleteNews(@PathVariable Long id) {
        newsService.delete(id);
        return ApiResponse.success("删除成功");
    }

    // ==================== 招聘管理 ====================
    @GetMapping("/jobs")
    public ApiResponse<List<Job>> listJobs() {
        return ApiResponse.success(jobService.findAll());
    }

    @PostMapping("/jobs")
    public ApiResponse<Job> createJob(@Valid @RequestBody Job job) {
        return ApiResponse.success(jobService.save(job));
    }

    @PutMapping("/jobs/{id}")
    public ApiResponse<Job> updateJob(@PathVariable Long id, @Valid @RequestBody Job job) {
        job.setId(id);
        return ApiResponse.success(jobService.save(job));
    }

    @DeleteMapping("/jobs/{id}")
    public ApiResponse<String> deleteJob(@PathVariable Long id) {
        jobService.delete(id);
        return ApiResponse.success("删除成功");
    }

    // ==================== 留言管理 ====================
    @GetMapping("/messages")
    public ApiResponse<List<Message>> listMessages() {
        return ApiResponse.success(messageService.findAll());
    }

    @DeleteMapping("/messages/{id}")
    public ApiResponse<String> deleteMessage(@PathVariable Long id) {
        messageService.delete(id);
        return ApiResponse.success("删除成功");
    }

    // ==================== 子公司管理 ====================
    @GetMapping("/subsidiaries")
    public ApiResponse<List<Subsidiary>> listSubsidiaries() {
        return ApiResponse.success(subsidiaryService.findAll());
    }

    @PostMapping("/subsidiaries")
    public ApiResponse<Subsidiary> createSubsidiary(@Valid @RequestBody Subsidiary subsidiary) {
        return ApiResponse.success(subsidiaryService.save(subsidiary));
    }

    @PutMapping("/subsidiaries/{id}")
    public ApiResponse<Subsidiary> updateSubsidiary(@PathVariable Long id, @Valid @RequestBody Subsidiary subsidiary) {
        subsidiary.setId(id);
        return ApiResponse.success(subsidiaryService.save(subsidiary));
    }

    @DeleteMapping("/subsidiaries/{id}")
    public ApiResponse<String> deleteSubsidiary(@PathVariable Long id) {
        subsidiaryService.delete(id);
        return ApiResponse.success("删除成功");
    }

    // ==================== 核心业务领域 ====================
    @GetMapping("/core-businesses")
    public ApiResponse<List<CoreBusiness>> listCoreBusinesses() {
        return ApiResponse.success(coreBusinessService.findAll());
    }

    @PostMapping("/core-businesses")
    public ApiResponse<CoreBusiness> createCoreBusiness(@Valid @RequestBody CoreBusiness coreBusiness) {
        return ApiResponse.success(coreBusinessService.save(coreBusiness));
    }

    @PutMapping("/core-businesses/{id}")
    public ApiResponse<CoreBusiness> updateCoreBusiness(@PathVariable Long id, @Valid @RequestBody CoreBusiness coreBusiness) {
        coreBusiness.setId(id);
        return ApiResponse.success(coreBusinessService.save(coreBusiness));
    }

    @DeleteMapping("/core-businesses/{id}")
    public ApiResponse<String> deleteCoreBusiness(@PathVariable Long id) {
        coreBusinessService.delete(id);
        return ApiResponse.success("删除成功");
    }

    // ==================== 管理团队 ====================
    @GetMapping("/team")
    public ApiResponse<List<TeamMember>> listTeam() {
        return ApiResponse.success(teamService.findAll());
    }

    @PostMapping("/team")
    public ApiResponse<TeamMember> createTeam(@Valid @RequestBody TeamMember member) {
        return ApiResponse.success(teamService.save(member));
    }

    @PutMapping("/team/{id}")
    public ApiResponse<TeamMember> updateTeam(@PathVariable Long id, @Valid @RequestBody TeamMember member) {
        member.setId(id);
        return ApiResponse.success(teamService.save(member));
    }

    @DeleteMapping("/team/{id}")
    public ApiResponse<String> deleteTeam(@PathVariable Long id) {
        teamService.delete(id);
        return ApiResponse.success("删除成功");
    }

    // ==================== 企业荣誉 ====================
    @GetMapping("/honors")
    public ApiResponse<List<Honor>> listHonors() {
        return ApiResponse.success(honorService.findAll());
    }

    @PostMapping("/honors")
    public ApiResponse<Honor> createHonor(@Valid @RequestBody Honor honor) {
        return ApiResponse.success(honorService.save(honor));
    }

    @PutMapping("/honors/{id}")
    public ApiResponse<Honor> updateHonor(@PathVariable Long id, @Valid @RequestBody Honor honor) {
        honor.setId(id);
        return ApiResponse.success(honorService.save(honor));
    }

    @DeleteMapping("/honors/{id}")
    public ApiResponse<String> deleteHonor(@PathVariable Long id) {
        honorService.delete(id);
        return ApiResponse.success("删除成功");
    }

    // ==================== 发展大事记 ====================
    @GetMapping("/milestones")
    public ApiResponse<List<Milestone>> listMilestones() {
        return ApiResponse.success(milestoneService.findAll());
    }

    @PostMapping("/milestones")
    public ApiResponse<Milestone> createMilestone(@Valid @RequestBody Milestone milestone) {
        return ApiResponse.success(milestoneService.save(milestone));
    }

    @PutMapping("/milestones/{id}")
    public ApiResponse<Milestone> updateMilestone(@PathVariable Long id, @Valid @RequestBody Milestone milestone) {
        milestone.setId(id);
        return ApiResponse.success(milestoneService.save(milestone));
    }

    @DeleteMapping("/milestones/{id}")
    public ApiResponse<String> deleteMilestone(@PathVariable Long id) {
        milestoneService.delete(id);
        return ApiResponse.success("删除成功");
    }

    // ==================== 官网页面文案（site_content） ====================
    @GetMapping("/content")
    public ApiResponse<List<SiteContent>> listContent() {
        return ApiResponse.success(siteContentService.findAll());
    }

    @PostMapping("/content")
    public ApiResponse<SiteContent> createContent(@Valid @RequestBody SiteContent sc) {
        return ApiResponse.success(siteContentService.save(sc));
    }

    @PutMapping("/content/{id}")
    public ApiResponse<SiteContent> updateContent(@PathVariable Long id, @Valid @RequestBody SiteContent sc) {
        sc.setId(id);
        return ApiResponse.success(siteContentService.save(sc));
    }

    @DeleteMapping("/content/{id}")
    public ApiResponse<String> deleteContent(@PathVariable Long id) {
        siteContentService.delete(id);
        return ApiResponse.success("删除成功");
    }

    // ==================== 合作伙伴管理 ====================
    @GetMapping("/partners")
    public ApiResponse<List<Partner>> listPartners() {
        return ApiResponse.success(partnerService.findAll());
    }

    @PostMapping("/partners")
    public ApiResponse<Partner> createPartner(@Valid @RequestBody Partner partner) {
        return ApiResponse.success(partnerService.save(partner));
    }

    @PutMapping("/partners/{id}")
    public ApiResponse<Partner> updatePartner(@PathVariable Long id, @Valid @RequestBody Partner partner) {
        partner.setId(id);
        return ApiResponse.success(partnerService.save(partner));
    }

    @DeleteMapping("/partners/{id}")
    public ApiResponse<String> deletePartner(@PathVariable Long id) {
        partnerService.delete(id);
        return ApiResponse.success("删除成功");
    }
    // ==================== 管理员账号管理（仅超级管理员组） ====================
    @GetMapping("/admins")
    public ApiResponse<List<Map<String, Object>>> listAdmins() {
        return ApiResponse.success(adminService.listAdmins());
    }

    @PostMapping("/admins")
    public ApiResponse<Map<String, Object>> createAdmin(@RequestBody Map<String, Object> params) {
        // 兼容 role（旧前端）和 groupId（新前端）
        Long groupId = resolveGroupId(params);
        return ApiResponse.success(adminService.createAdmin(
                (String) params.get("username"),
                (String) params.get("password"),
                (String) params.get("nickname"),
                groupId));
    }

    @PutMapping("/admins/{id}")
    public ApiResponse<Map<String, Object>> updateAdmin(@PathVariable Long id, @RequestBody Map<String, Object> params) {
        Long groupId = resolveGroupId(params);
        return ApiResponse.success(adminService.updateAdmin(
                id,
                (String) params.get("nickname"),
                groupId,
                (String) params.get("password")));
    }

    /** 兼容 role 和 groupId 两种入参 */
    private Long resolveGroupId(Map<String, Object> params) {
        if (params.containsKey("groupId") && params.get("groupId") != null) {
            if (params.get("groupId") instanceof Number) {
                return ((Number) params.get("groupId")).longValue();
            }
            try {
                return Long.parseLong(params.get("groupId").toString());
            } catch (NumberFormatException ignored) {}
        }
        // 兼容旧 role 字段
        String role = (String) params.get("role");
        if ("SUPER_ADMIN".equals(role)) return 1L;
        if ("ADMIN".equals(role)) return 2L;
        if ("EDITOR".equals(role)) return 3L;
        return null;
    }

    @DeleteMapping("/admins/{id}")
    public ApiResponse<String> deleteAdmin(@PathVariable Long id, HttpServletRequest request) {
        String currentUsername = (String) request.getAttribute("adminUsername");
        adminService.deleteAdmin(id, currentUsername);
        return ApiResponse.success("删除成功");
    }

    // ==================== 修改密码 ====================
    @PostMapping("/password")
    public ApiResponse<String> changePassword(@RequestBody Map<String, String> params, HttpServletRequest request) {
        // 用户名必须取自服务端 JWT 解析结果，禁止信任前端传入（防越权改他人密码）
        String username = (String) request.getAttribute("adminUsername");
        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");
        adminService.changePassword(username, oldPassword, newPassword);
        return ApiResponse.success("密码修改成功");
    }
}


