package com.ningshang.config;

import com.ningshang.entity.SiteContent;
import com.ningshang.service.SiteContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class SiteContentModelAdvice {
    @Autowired
    private SiteContentService siteContentService;

    @ModelAttribute("siteContent")
    public Map<String, String> siteContent() {
        return siteContentService.findAll().stream()
                .collect(Collectors.toMap(SiteContent::getContentKey, SiteContent::getContent, (a, b) -> a));
    }
}
