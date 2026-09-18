package com.ningshang.repository;
import com.ningshang.entity.AdminGroupPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.*;
public interface AdminGroupPermissionRepository extends JpaRepository<AdminGroupPermission, Long> {
    List<AdminGroupPermission> findByGroupId(Long groupId);
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM AdminGroupPermission p WHERE p.groupId = :groupId")
    void deleteByGroupId(@Param("groupId") Long groupId);
    boolean existsByGroupIdAndPermissionId(Long groupId, Long permissionId);
}
