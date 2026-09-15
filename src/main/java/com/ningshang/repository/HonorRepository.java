package com.ningshang.repository;

import com.ningshang.entity.Honor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HonorRepository extends JpaRepository<Honor, Long> {
    List<Honor> findAllByOrderBySortOrderAscIdAsc();
    List<Honor> findAllByOrderBySortOrderAsc();
    List<Honor> findByTitleContainingOrderByCreatedAtDesc(String title);
}
