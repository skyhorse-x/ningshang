package com.ningshang.repository;

import com.ningshang.entity.AdminMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminMenuRepository extends JpaRepository<AdminMenu, Long> {
    List<AdminMenu> findAllByOrderByParentIdAscSortOrderAsc();
    List<AdminMenu> findByParentIdOrderBySortOrderAsc(Long parentId);
    List<AdminMenu> findByIdIn(List<Long> ids);
    long countByParentId(Long parentId);
    Optional<AdminMenu> findByPath(String path);
    Optional<AdminMenu> findByParentIdAndName(Long parentId, String name);
}
