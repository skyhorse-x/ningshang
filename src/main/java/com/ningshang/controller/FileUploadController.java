package com.ningshang.controller;

import com.ningshang.exception.BusinessException;
import com.ningshang.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * 富文本图片上传：图片保存到本地文件目录，正文只存 URL（替代 Base64 内联）
 */
@RestController
@RequestMapping("/api/admin")
public class FileUploadController {

    @Value("${app.upload-dir:./uploads}")
    private String uploadDir;

    private static final Set<String> ALLOWED_EXT = new HashSet<>(java.util.Arrays.asList(
            "jpg", "jpeg", "png", "gif", "webp"));
    private static final Set<String> ALLOWED_VIDEO_EXT = new HashSet<>(java.util.Arrays.asList(
            "mp4", "webm", "ogg", "mov"));

    @PostMapping("/upload")
    public ApiResponse<Map<String, String>> upload(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(400, "请选择要上传的图片");
        }
        String original = file.getOriginalFilename() == null ? "" : file.getOriginalFilename();
        String ext = "";
        int dot = original.lastIndexOf('.');
        if (dot >= 0) {
            ext = original.substring(dot + 1).toLowerCase(Locale.ROOT);
        }
        if (!ALLOWED_EXT.contains(ext)) {
            throw new BusinessException(400, "仅支持 jpg/jpeg/png/gif/webp 格式图片");
        }
        try {
            Path dir = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(dir);
            String filename = System.currentTimeMillis() + "-"
                    + java.util.UUID.randomUUID().toString().substring(0, 8) + "." + ext;
            file.transferTo(dir.resolve(filename).toFile());
            Map<String, String> data = new HashMap<>();
            data.put("url", "/uploads/" + filename);
            return ApiResponse.success(data);
        } catch (IOException e) {
            throw new BusinessException(400, "图片保存失败，请稍后重试");
        }
    }

    @PostMapping("/upload/video")
    public ApiResponse<Map<String, String>> uploadVideo(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(400, "请选择要上传的视频");
        }
        if (file.getSize() > 100L * 1024 * 1024) {
            throw new BusinessException(400, "视频不能超过 100MB");
        }
        String original = file.getOriginalFilename() == null ? "" : file.getOriginalFilename();
        int dot = original.lastIndexOf('.');
        String ext = dot >= 0 ? original.substring(dot + 1).toLowerCase(Locale.ROOT) : "";
        String contentType = file.getContentType() == null ? "" : file.getContentType().toLowerCase(Locale.ROOT);
        if (!ALLOWED_VIDEO_EXT.contains(ext) || !contentType.startsWith("video/")) {
            throw new BusinessException(400, "仅支持 mp4/webm/ogg/mov 格式视频");
        }
        try {
            Path dir = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(dir);
            String filename = System.currentTimeMillis() + "-"
                    + java.util.UUID.randomUUID().toString().substring(0, 8) + "." + ext;
            Path target = dir.resolve(filename).normalize();
            if (!target.startsWith(dir)) {
                throw new BusinessException(400, "视频文件名无效");
            }
            file.transferTo(target.toFile());
            Map<String, String> data = new HashMap<>();
            data.put("url", "/uploads/" + filename);
            return ApiResponse.success(data);
        } catch (IOException e) {
            throw new BusinessException(400, "视频保存失败，请稍后重试");
        }
    }
}
