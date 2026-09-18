package com.ningshang.repository;

import com.ningshang.entity.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PartnerRepository extends JpaRepository<Partner, Long> {
    List<Partner> findAllByOrderBySortOrderAscIdAsc();
    List<Partner> findByStatusOrderBySortOrderAscIdAsc(Integer status);
}
