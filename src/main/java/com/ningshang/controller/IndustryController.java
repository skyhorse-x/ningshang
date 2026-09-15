package com.ningshang.controller;

import com.ningshang.dto.ApiResponse;
import com.ningshang.entity.Subsidiary;
import com.ningshang.service.SubsidiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class IndustryController {

    @Autowired
    private SubsidiaryService subsidiaryService;

    @GetMapping("/industry")
    public String industry(Model model) {
        model.addAttribute("subsidiaries", subsidiaryService.findAll());
        model.addAttribute("currentPage", "industry");
        return "pages/industry";
    }

    @GetMapping("/industry-construction")
    public String industryConstruction(Model model) {
        model.addAttribute("currentPage", "industry");
        return "pages/industry-construction";
    }

    @GetMapping("/industry-software")
    public String industrySoftware(Model model) {
        model.addAttribute("currentPage", "industry");
        return "pages/industry-software";
    }

    @GetMapping("/api/subsidiaries")
    @ResponseBody
    public ApiResponse<List<Subsidiary>> subsidiariesApi() {
        return ApiResponse.success(subsidiaryService.findAll());
    }
}
