package com.ningshang.entity;

import lombok.Data;
import javax.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;

/**
 * 官网页面文案配置（键值对）
 * 覆盖：集团简介、董事长致词、企业文化、党建工作、联系方式、统计数字等页面内容
 */
@Data
@Entity
@Table(name = "site_content", uniqueConstraints = @UniqueConstraint(columnNames = "contentKey"))
public class SiteContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Column(name = "contentKey", nullable = false, length = 100, unique = true)
    @NotBlank
    @Size(max = 100)
    private String contentKey;

    @Column(length = 100)
    @Size(max = 100)
    private String title;

    @Column(columnDefinition = "LONGTEXT")
    @Size(max = 1000000)
    private String content;

    private Integer sortOrder;
}
