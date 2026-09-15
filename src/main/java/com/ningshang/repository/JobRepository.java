package com.ningshang.repository;

import com.ningshang.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findAllByOrderBySortOrderAscIdAsc();
    List<Job> findAllByOrderBySortOrderAsc();
    List<Job> findByTitleContainingOrderByCreatedAtDesc(String title);
}
