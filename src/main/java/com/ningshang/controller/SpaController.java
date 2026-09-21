package com.ningshang.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Vue history-mode fallback. Spring Boot only serves APIs and files; every
 * browser page route receives the same Vue entry document.
 */
@Controller
public class SpaController {

    @GetMapping({
            "/",
            "/about/**",
            "/news",
            "/news/{id}",
            "/industry",
            "/industry/{id}",
            "/contact",
            "/contact/message",
            "/recruit",
            "/recruit/jobs",
            "/ningshang-admin",
            "/ningshang-admin/**"
    })
    public String vueApp() {
        return "forward:/index.html";
    }
}
