package com.ningshang.repository;

import com.ningshang.entity.AdminGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminGroupRepository extends JpaRepository<AdminGroup, Long> {
    Optional<AdminGroup> findByName(String name);
    List<AdminGroup> findByStatusOrderBySortOrderAsc(Integer status);
    boolean existsByName(String name);
}
