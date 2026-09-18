package com.ningshang.controller;

import com.ningshang.dto.ApiResponse;
import com.ningshang.entity.Subsidiary;
import com.ningshang.exception.BusinessException;
import com.ningshang.service.SubsidiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * 成员企业详情页。
     * id 限定为纯数字，避免与 /industry-construction、/industry-software 之类的旧路径冲突。
     */
    @GetMapping("/industry/{id:[0-9]+}")
    public String subsidiaryDetail(@PathVariable("id") Long id, Model model) {
        Subsidiary subsidiary = subsidiaryService.findById(id);
        model.addAttribute("sub", subsidiary);
        model.addAttribute("others", subsidiaryService.findAll().stream()
                .filter(item -> !id.equals(item.getId()))
                .collect(Collectors.toList()));
        model.addAttribute("currentPage", "industry");
        return "pages/subsidiary-detail";
    }

    @GetMapping("/industry-construction")
    public String industryConstruction(Model model) {
        return "redirect:/industry";
    }

    @GetMapping("/industry-software")
    public String industrySoftware(Model model) {
        return "redirect:/industry";
    }

    @GetMapping("/api/subsidiaries")
    @ResponseBody
    public ApiResponse<List<Subsidiary>> subsidiariesApi() {
        return ApiResponse.success(subsidiaryService.findAll());
    }

    @GetMapping("/api/subsidiaries/{id:[0-9]+}")
    @ResponseBody
    public ApiResponse<Subsidiary> subsidiaryDetailApi(@PathVariable("id") Long id) {
        Subsidiary subsidiary = subsidiaryService.findById(id);
        if (subsidiary == null) throw new BusinessException(404, "成员企业未找到");
        return ApiResponse.success(subsidiary);
    }
}
