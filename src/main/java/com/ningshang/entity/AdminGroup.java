package com.ningshang.entity;

import lombok.Data;
import javax.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 权限组（替代固定角色 SUPER_ADMIN/ADMIN/EDITOR）
 */
@Data
@Entity
@Table(name = "admin_group")
public class AdminGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    @NotBlank
    @Size(max = 100)
    private String name;

    @Column(length = 500)
    @Size(max = 500)
    private String description;

    /** 1=启用 0=禁用 */
    @Column(nullable = false, columnDefinition = "tinyint default 1")
    @NotNull
    @Min(0)
    @Max(1)
    private Integer status = 1;

    @Column(name = "sort_order", columnDefinition = "int default 0")
    private Integer sortOrder = 0;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
