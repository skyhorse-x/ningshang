package com.ningshang.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LegacyRedirectController {
    @GetMapping("/about-intro.html")
    public String aboutIntro() { return "redirect:/about/intro"; }

    @GetMapping("/about-speech.html")
    public String aboutSpeech() { return "redirect:/about/speech"; }

    @GetMapping("/about-events.html")
    public String aboutEvents() { return "redirect:/about/events"; }

    @GetMapping("/about-team.html")
    public String aboutTeam() { return "redirect:/about/team"; }

    @GetMapping("/about-honor.html")
    public String aboutHonor() { return "redirect:/about/honor"; }

    @GetMapping("/about-party.html")
    public String aboutParty() { return "redirect:/about/party"; }

    @GetMapping("/about-culture.html")
    public String aboutCulture() { return "redirect:/about/culture"; }

    @GetMapping("/news.html")
    public String news() { return "redirect:/news"; }

    @GetMapping("/news-detail.html")
    public String newsDetail(@RequestParam(required = false) String id) {
        return id == null || id.isBlank() ? "redirect:/news" : "redirect:/news/" + id;
    }

    @GetMapping("/industry.html")
    public String industry() { return "redirect:/industry"; }

    @GetMapping("/industry-construction.html")
    public String industryConstruction() { return "redirect:/industry"; }

    @GetMapping("/industry-software.html")
    public String industrySoftware() { return "redirect:/industry"; }

    @GetMapping("/contact.html")
    public String contact() { return "redirect:/contact"; }

    @GetMapping("/contact-message.html")
    public String contactMessage() { return "redirect:/contact/message"; }

    @GetMapping("/recruit.html")
    public String recruit() { return "redirect:/recruit"; }

    @GetMapping("/recruit-job.html")
    public String recruitJob() { return "redirect:/recruit/jobs"; }

    @GetMapping({"/about-intro", "/about-speech", "/about-events", "/about-team", "/about-honor", "/about-party", "/about-culture"})
    public String oldAboutRoutes(javax.servlet.http.HttpServletRequest request) {
        return "redirect:" + request.getRequestURI().replace("/about-", "/about/");
    }

    @GetMapping("/contact-message")
    public String oldContactMessage() { return "redirect:/contact/message"; }

    @GetMapping("/recruit-job")
    public String oldRecruitJob() { return "redirect:/recruit/jobs"; }

    @GetMapping("/ningshang-admin-ui/**")
    public String oldAdminUi() { return "redirect:/ningshang-admin/login"; }
}
