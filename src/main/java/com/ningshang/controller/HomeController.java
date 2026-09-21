package com.ningshang.controller;

import com.ningshang.dto.ApiResponse;
import com.ningshang.service.CoreBusinessService;
import com.ningshang.service.NewsService;
import com.ningshang.service.PartnerService;
import com.ningshang.service.SubsidiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class HomeController {

    @Autowired
    private NewsService newsService;

    @Autowired
    private SubsidiaryService subsidiaryService;

    @Autowired
    private CoreBusinessService coreBusinessService;

    @Autowired
    private PartnerService partnerService;

    @GetMapping("/api/home")
    public ApiResponse<Map<String, Object>> homeApi() {
        Map<String, Object> data = new HashMap<>();
        data.put("news", newsService.findAll());
        data.put("subsidiaries", subsidiaryService.findAll());
        data.put("coreBusinesses", coreBusinessService.findAll());
        data.put("partners", partnerService.findEnabled());
        return ApiResponse.success(data);
    }
}


