package com.ningshang.controller;

import com.ningshang.dto.ApiResponse;
import com.ningshang.entity.*;
import com.ningshang.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RestController
public class AboutController {

    @Autowired private TeamService teamService;
    @Autowired private HonorService honorService;
    @Autowired private MilestoneService milestoneService;
    @Autowired private SiteContentService siteContentService;

    // API endpoints
    @GetMapping("/api/team")
    public ApiResponse<List<TeamMember>> teamApi() {
        return ApiResponse.success(teamService.findAll());
    }

    @GetMapping("/api/honors")
    public ApiResponse<List<Honor>> honorsApi() {
        return ApiResponse.success(honorService.findAll());
    }

    @GetMapping("/api/milestones")
    public ApiResponse<List<Milestone>> milestonesApi() {
        return ApiResponse.success(milestoneService.findAll());
    }

    @GetMapping("/api/content")
    public ApiResponse<List<SiteContent>> contentApi() {
        return ApiResponse.success(siteContentService.findAll());
    }
}
