package com.ningshang.controller;

import com.ningshang.dto.ApiResponse;
import com.ningshang.entity.News;
import com.ningshang.entity.Subsidiary;
import com.ningshang.service.NewsService;
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

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("newsList", newsService.findAll());
        model.addAttribute("subsidiaries", subsidiaryService.findAll());
        model.addAttribute("currentPage", "home");
        return "pages/index";
    }

    @GetMapping("/api/home")
    @ResponseBody
    public ApiResponse<Map<String, Object>> homeApi() {
        Map<String, Object> data = new HashMap<>();
        data.put("news", newsService.findAll());
        data.put("subsidiaries", subsidiaryService.findAll());
        return ApiResponse.success(data);
    }
}
