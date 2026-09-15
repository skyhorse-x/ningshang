package com.ningshang.controller;

import com.ningshang.dto.ApiResponse;
import com.ningshang.exception.BusinessException;
import com.ningshang.entity.News;
import com.ningshang.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping("/news")
    public String newsList(Model model) {
        model.addAttribute("newsList", newsService.findAll());
        model.addAttribute("currentPage", "news");
        return "pages/news";
    }

    @GetMapping("/news-detail")
    public String newsDetail(@RequestParam("id") String id, Model model) {
        News news = newsService.findByNewsId(id);
        model.addAttribute("news", news);
        model.addAttribute("currentPage", "news");
        return "pages/news-detail";
    }

    @GetMapping("/api/news")
    @ResponseBody
    public ApiResponse<List<News>> newsApi() {
        return ApiResponse.success(newsService.findAll());
    }

    @GetMapping("/api/news/{newsId}")
    @ResponseBody
    public ApiResponse<News> newsDetailApi(@PathVariable("newsId") String newsId) {
        News news = newsService.findByNewsId(newsId);
        if (news == null) throw new BusinessException(404, "文章未找到");
        return ApiResponse.success(news);
    }
}
