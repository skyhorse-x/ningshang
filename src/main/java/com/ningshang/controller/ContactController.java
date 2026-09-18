package com.ningshang.controller;

import com.ningshang.dto.ApiResponse;
import com.ningshang.entity.Job;
import com.ningshang.entity.Message;
import com.ningshang.service.JobService;
import com.ningshang.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
public class ContactController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private JobService jobService;

    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("currentPage", "contact");
        return "pages/contact";
    }

    @GetMapping("/contact-message")
    public String contactMessage(Model model) {
        model.addAttribute("currentPage", "contact");
        return "pages/contact-message";
    }

    @GetMapping("/recruit")
    public String recruit(Model model) {
        model.addAttribute("currentPage", "contact");
        return "pages/recruit";
    }

    @GetMapping("/recruit-job")
    public String recruitJob(Model model) {
        model.addAttribute("jobs", jobService.findAll());
        model.addAttribute("currentPage", "contact");
        return "pages/recruit-job";
    }

    @PostMapping("/api/messages")
    @ResponseBody
    public ApiResponse<String> submitMessage(@Valid @RequestBody Message message) {
        messageService.save(message);
        return ApiResponse.success("留言提交成功");
    }

    @GetMapping("/api/jobs")
    @ResponseBody
    public ApiResponse<List<Job>> jobsApi() {
        return ApiResponse.success(jobService.findAll());
    }
}
