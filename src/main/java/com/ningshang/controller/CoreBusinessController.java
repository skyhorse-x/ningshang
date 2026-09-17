package com.ningshang.controller;

import com.ningshang.dto.ApiResponse;
import com.ningshang.entity.CoreBusiness;
import com.ningshang.service.CoreBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CoreBusinessController {
    @Autowired
    private CoreBusinessService coreBusinessService;

    @GetMapping("/api/core-businesses")
    public ApiResponse<List<CoreBusiness>> coreBusinessesApi() {
        return ApiResponse.success(coreBusinessService.findAll());
    }
}
