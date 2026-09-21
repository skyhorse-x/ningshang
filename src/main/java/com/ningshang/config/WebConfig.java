package com.ningshang.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.WebContentInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AdminAuthInterceptor adminAuthInterceptor;

    @org.springframework.beans.factory.annotation.Value("${app.upload-dir:./uploads}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/");
        registry.addResourceHandler("/ningshang-admin-ui/**")
                .addResourceLocations("file:frontend/dist/", "classpath:/static/ningshang-admin-ui/");
        // 富文本上传的图片目录映射到 /uploads/**
        String dir = java.nio.file.Paths.get(uploadDir).toAbsolutePath().normalize().toString().replace("\\", "/");
        if (!dir.endsWith("/")) {
            dir = dir + "/";
        }
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + dir);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        WebContentInterceptor apiNoCache = new WebContentInterceptor();
        apiNoCache.setCacheSeconds(0);
        apiNoCache.setUseExpiresHeader(true);
        apiNoCache.setUseCacheControlHeader(true);
        apiNoCache.setUseCacheControlNoStore(true);
        registry.addInterceptor(apiNoCache).addPathPatterns("/api/**");

        // 页面 HTML 也禁止缓存：否则改了模板/样式，浏览器仍显示旧页面，
        // 看起来像"改了没生效"（静态资源靠 ?v= 版本号刷新，这里只管页面本身）
        WebContentInterceptor pageNoCache = new WebContentInterceptor();
        pageNoCache.setCacheSeconds(0);
        pageNoCache.setUseExpiresHeader(true);
        pageNoCache.setUseCacheControlHeader(true);
        pageNoCache.setUseCacheControlNoStore(true);
        registry.addInterceptor(pageNoCache)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/api/**", "/error", "/favicon.ico",
                        "/css/**", "/js/**", "/images/**", "/uploads/**",
                        "/vendor/**", "/static/**",
                        "/ningshang-admin-ui/assets/**");

        registry.addInterceptor(adminAuthInterceptor)
                .addPathPatterns("/api/admin/**");
    }
}
