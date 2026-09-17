package com.ningshang.repository;

import com.ningshang.entity.CoreBusiness;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoreBusinessRepository extends JpaRepository<CoreBusiness, Long> {
    List<CoreBusiness> findAllByOrderBySortOrderAscIdAsc();
}
