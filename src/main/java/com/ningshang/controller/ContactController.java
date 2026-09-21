package com.ningshang.controller;

import com.ningshang.dto.ApiResponse;
import com.ningshang.entity.Job;
import com.ningshang.entity.Message;
import com.ningshang.service.JobService;
import com.ningshang.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ContactController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private JobService jobService;

    @PostMapping("/api/messages")
    public ApiResponse<String> submitMessage(@Valid @RequestBody Message message) {
        messageService.save(message);
        return ApiResponse.success("留言提交成功");
    }

    @GetMapping("/api/jobs")
    public ApiResponse<List<Job>> jobsApi() {
        return ApiResponse.success(jobService.findAll());
    }
}
