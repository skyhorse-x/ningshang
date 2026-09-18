package com.ningshang.controller;

import com.ningshang.dto.ApiResponse;
import com.ningshang.entity.Partner;
import com.ningshang.service.PartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PartnerController {
    @Autowired
    private PartnerService partnerService;

    @GetMapping("/api/partners")
    public ApiResponse<List<Partner>> partners() {
        return ApiResponse.success(partnerService.findEnabled());
    }
}
