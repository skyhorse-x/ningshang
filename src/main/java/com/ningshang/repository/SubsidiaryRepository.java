package com.ningshang.repository;

import com.ningshang.entity.Subsidiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SubsidiaryRepository extends JpaRepository<Subsidiary, Long> {
    List<Subsidiary> findAllByOrderBySortOrderAscIdAsc();
    List<Subsidiary> findAllByOrderBySortOrderAsc();
    List<Subsidiary> findByNameContainingOrderByCreatedAtDesc(String name);
}
