package com.ningshang.config;

import com.ningshang.entity.SiteContent;
import com.ningshang.entity.Subsidiary;
import com.ningshang.service.SiteContentService;
import com.ningshang.service.SubsidiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class SiteContentModelAdvice {
    @Autowired
    private SiteContentService siteContentService;

    @Autowired
    private SubsidiaryService subsidiaryService;

    @ModelAttribute("siteContent")
    public Map<String, String> siteContent() {
        return siteContentService.findAll().stream()
                .collect(Collectors.toMap(SiteContent::getContentKey, SiteContent::getContent, (a, b) -> a));
    }

    /**
     * 子公司列表：顶部导航「集团产业」的下级菜单由它渲染（header 是公用 fragment，需全站可用）。
     * 各页面控制器若自行 addAttribute("subsidiaries")，以控制器的为准，取值相同。
     */
    @ModelAttribute("subsidiaries")
    public List<Subsidiary> subsidiaries() {
        return subsidiaryService.findAll();
    }
}
