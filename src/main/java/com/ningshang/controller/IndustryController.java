package com.ningshang.controller;

import com.ningshang.dto.ApiResponse;
import com.ningshang.entity.Subsidiary;
import com.ningshang.exception.BusinessException;
import com.ningshang.service.SubsidiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
public class IndustryController {

    @Autowired
    private SubsidiaryService subsidiaryService;


    @GetMapping("/api/subsidiaries")
    public ApiResponse<List<Subsidiary>> subsidiariesApi() {
        return ApiResponse.success(subsidiaryService.findAll());
    }

    @GetMapping("/api/subsidiaries/{id:[0-9]+}")
    public ApiResponse<Subsidiary> subsidiaryDetailApi(@PathVariable("id") Long id) {
        Subsidiary subsidiary = subsidiaryService.findById(id);
        if (subsidiary == null) throw new BusinessException(404, "成员企业未找到");
        return ApiResponse.success(subsidiary);
    }
}
