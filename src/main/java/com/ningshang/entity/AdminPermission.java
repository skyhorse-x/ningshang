package com.ningshang.entity;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "admin_permission", uniqueConstraints = @UniqueConstraint(columnNames = "code"))
public class AdminPermission {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 50) private String module;
    @Column(name = "module_name", nullable = false, length = 50) private String moduleName;
    @Column(nullable = false, length = 30) private String action;
    @Column(nullable = false, length = 100) private String code;
    @Column(nullable = false, length = 50) private String name;
    @Column(name = "sort_order") private Integer sortOrder = 0;
    private Integer status = 1;
}
