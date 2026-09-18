package com.ningshang.controller;

import com.ningshang.dto.ApiResponse;
import com.ningshang.entity.News;
import com.ningshang.entity.Subsidiary;
import com.ningshang.service.CoreBusinessService;
import com.ningshang.service.NewsService;
import com.ningshang.service.PartnerService;
import com.ningshang.service.SubsidiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    private NewsService newsService;

    @Autowired
    private SubsidiaryService subsidiaryService;

    @Autowired
    private CoreBusinessService coreBusinessService;

    @Autowired
    private PartnerService partnerService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("newsList", newsService.findAll());
        model.addAttribute("subsidiaries", subsidiaryService.findAll());
        model.addAttribute("coreBusinesses", coreBusinessService.findAll());
        model.addAttribute("partners", partnerService.findEnabled());
        model.addAttribute("currentPage", "home");
        return "pages/index";
    }

    @GetMapping("/api/home")
    @ResponseBody
    public ApiResponse<Map<String, Object>> homeApi() {
        Map<String, Object> data = new HashMap<>();
        data.put("news", newsService.findAll());
        data.put("subsidiaries", subsidiaryService.findAll());
        data.put("coreBusinesses", coreBusinessService.findAll());
        data.put("partners", partnerService.findEnabled());
        return ApiResponse.success(data);
    }
}


