package com.ningshang.repository;
import com.ningshang.entity.AdminGroupPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface AdminGroupPermissionRepository extends JpaRepository<AdminGroupPermission, Long> {
    List<AdminGroupPermission> findByGroupId(Long groupId);
    void deleteByGroupId(Long groupId);
    boolean existsByGroupIdAndPermissionId(Long groupId, Long permissionId);
}
