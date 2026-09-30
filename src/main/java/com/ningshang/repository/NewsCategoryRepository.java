package com.ningshang.repository;

import com.ningshang.entity.NewsCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewsCategoryRepository extends JpaRepository<NewsCategory, Long> {
    List<NewsCategory> findAllByOrderBySortOrderAsc();

    boolean existsByKey(String key);

    Optional<NewsCategory> findByKey(String key);
}
