package com.ningshang.repository;

import com.ningshang.entity.SiteContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SiteContentRepository extends JpaRepository<SiteContent, Long> {
    List<SiteContent> findAllByOrderBySortOrderAsc();
    Optional<SiteContent> findByContentKey(String contentKey);
}
