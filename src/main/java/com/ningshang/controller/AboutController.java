package com.ningshang.controller;

import com.ningshang.dto.ApiResponse;
import com.ningshang.entity.*;
import com.ningshang.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class AboutController {

    @Autowired private TeamService teamService;
    @Autowired private HonorService honorService;
    @Autowired private MilestoneService milestoneService;
    @Autowired private SubsidiaryService subsidiaryService;
    @Autowired private SiteContentService siteContentService;

    @GetMapping("/about-intro")
    public String aboutIntro(Model model) {
        model.addAttribute("subsidiaries", subsidiaryService.findAll());
        model.addAttribute("currentPage", "about");
        return "pages/about-intro";
    }

    @GetMapping("/about-speech")
    public String aboutSpeech(Model model) {
        Map<String, String> content = siteContentService.findAll().stream()
                .collect(Collectors.toMap(SiteContent::getContentKey, SiteContent::getContent, (a, b) -> a));
        model.addAttribute("content", content);
        model.addAttribute("currentPage", "about");
        return "pages/about-speech";
    }

    @GetMapping("/about-events")
    public String aboutEvents(Model model) {
        model.addAttribute("milestones", milestoneService.findAll());
        model.addAttribute("currentPage", "about");
        return "pages/about-events";
    }

    @GetMapping("/about-team")
    public String aboutTeam(Model model) {
        model.addAttribute("teamMembers", teamService.findAll());
        model.addAttribute("currentPage", "about");
        return "pages/about-team";
    }

    @GetMapping("/about-honor")
    public String aboutHonor(Model model) {
        model.addAttribute("honors", honorService.findAll());
        model.addAttribute("currentPage", "about");
        return "pages/about-honor";
    }

    @GetMapping("/about-party")
    public String aboutParty(Model model) {
        model.addAttribute("currentPage", "about");
        return "pages/about-party";
    }

    @GetMapping("/about-culture")
    public String aboutCulture(Model model) {
        model.addAttribute("currentPage", "about");
        return "pages/about-culture";
    }

    // API endpoints
    @GetMapping("/api/team")
    @ResponseBody
    public ApiResponse<List<TeamMember>> teamApi() {
        return ApiResponse.success(teamService.findAll());
    }

    @GetMapping("/api/honors")
    @ResponseBody
    public ApiResponse<List<Honor>> honorsApi() {
        return ApiResponse.success(honorService.findAll());
    }

    @GetMapping("/api/milestones")
    @ResponseBody
    public ApiResponse<List<Milestone>> milestonesApi() {
        return ApiResponse.success(milestoneService.findAll());
    }

    @GetMapping("/api/content")
    @ResponseBody
    public ApiResponse<List<SiteContent>> contentApi() {
        return ApiResponse.success(siteContentService.findAll());
    }
}
