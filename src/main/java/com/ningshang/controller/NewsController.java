package com.ningshang.controller;

import com.ningshang.dto.ApiResponse;
import com.ningshang.exception.BusinessException;
import com.ningshang.entity.News;
import com.ningshang.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
public class NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping("/api/news")
    public ApiResponse<List<News>> newsApi() {
        return ApiResponse.success(newsService.findAll());
    }

    @GetMapping("/api/news/{newsId}")
    public ApiResponse<News> newsDetailApi(@PathVariable("newsId") String newsId) {
        News news = newsService.findByNewsId(newsId);
        if (news == null) throw new BusinessException(404, "文章未找到");
        return ApiResponse.success(news);
    }
}
