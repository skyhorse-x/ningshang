package com.ningshang.entity;

import lombok.Data;
import javax.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 后台导航菜单（两级结构）
 * parent_id = 0 表示一级分组，> 0 表示二级菜单项
 */
@Data
@Entity
@Table(name = "admin_menu")
public class AdminMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Column(name = "parent_id", nullable = false, columnDefinition = "bigint default 0")
    @NotNull
    @Min(0)
    private Long parentId = 0L;

    @Column(nullable = false, length = 100)
    @NotBlank
    @Size(max = 100)
    private String name;

    /** 路由路径，如 /ningshang-admin/news */
    @Column(length = 200)
    @Size(max = 200)
    private String path;

    /** Element Plus 图标类名，如 Document、Setting */
    @Column(length = 100)
    @Size(max = 100)
    private String icon;

    @Column(name = "sort_order", columnDefinition = "int default 0")
    private Integer sortOrder = 0;

    /** 1=启用 0=禁用 */
    @Column(nullable = false, columnDefinition = "tinyint default 1")
    @NotNull
    @Min(0)
    @Max(1)
    private Integer status = 1;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;

    @Transient
    private List<AdminMenu> children;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
