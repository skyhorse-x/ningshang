package com.ningshang.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 权限组-菜单关联（多对多）
 */
@Data
@Entity
@Table(name = "admin_group_menu",
       uniqueConstraints = @UniqueConstraint(columnNames = {"group_id", "menu_id"}))
public class AdminGroupMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_id", nullable = false)
    private Long groupId;

    @Column(name = "menu_id", nullable = false)
    private Long menuId;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
