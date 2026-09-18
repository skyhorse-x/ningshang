package com.ningshang.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminUiController {
    @GetMapping({"/ningshang-admin-ui", "/ningshang-admin-ui/", "/ningshang-admin-ui/{path:[^\\.]*}"})
    public String adminUi() {
        return "forward:/ningshang-admin-ui/index.html";
    }
}
