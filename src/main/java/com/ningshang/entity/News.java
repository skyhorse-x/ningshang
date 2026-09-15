package com.ningshang.entity;

import lombok.Data;
import javax.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Entity
@Table(name = "news")
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    
    @Column(nullable = false, length = 200)
    @NotBlank
    @Size(max = 200)
    private String title;
    
    @Column(length = 500)
    @Size(max = 500)
    private String summary;
    
    @Column(columnDefinition = "LONGTEXT")
    private String body;
    
    @Column(length = 50)
    @Size(max = 50)
    private String category;
    
    @Column(length = 50)
    @Size(max = 50)
    private String categoryName;
    
    @Column(length = 50)
    @Size(max = 50)
    private String date;
    
    @Column(length = 100)
    @Size(max = 100)
    private String author;
    
    @Column(length = 100)
    @Size(max = 100)
    private String source;
    
    @Column(length = 200)
    @Size(max = 200)
    private String image;
    
    @Column(length = 50)
    @Size(max = 50)
    private String newsId;
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        // 自动设置为当前年月（格式：2026-09）
        if (date == null || date.isEmpty()) {
            date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        }
    }
}
