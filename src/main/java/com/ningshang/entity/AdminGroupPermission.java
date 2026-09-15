package com.ningshang.entity;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "admin_group_permission", uniqueConstraints = @UniqueConstraint(columnNames = {"group_id", "permission_id"}))
public class AdminGroupPermission {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "group_id", nullable = false) private Long groupId;
    @Column(name = "permission_id", nullable = false) private Long permissionId;
}
