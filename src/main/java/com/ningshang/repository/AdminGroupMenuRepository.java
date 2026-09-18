package com.ningshang.repository;

import com.ningshang.entity.AdminGroupMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminGroupMenuRepository extends JpaRepository<AdminGroupMenu, Long> {
    List<AdminGroupMenu> findByGroupId(Long groupId);
    List<AdminGroupMenu> findByGroupIdIn(List<Long> groupIds);
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM AdminGroupMenu m WHERE m.groupId = :groupId")
    void deleteByGroupId(@Param("groupId") Long groupId);
    void deleteByMenuIdIn(List<Long> menuIds);

    @Query("SELECT agm.menuId FROM AdminGroupMenu agm WHERE agm.groupId = :groupId")
    List<Long> findMenuIdByGroupId(@Param("groupId") Long groupId);
}
