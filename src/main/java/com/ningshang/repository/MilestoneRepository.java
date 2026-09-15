package com.ningshang.repository;

import com.ningshang.entity.Milestone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MilestoneRepository extends JpaRepository<Milestone, Long> {
    List<Milestone> findAllByOrderBySortOrderAscIdAsc();
    List<Milestone> findAllByOrderBySortOrderAsc();
    List<Milestone> findByTitleContainingOrderByCreatedAtDesc(String title);
}
