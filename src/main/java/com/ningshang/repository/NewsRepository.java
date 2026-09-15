package com.ningshang.repository;

import com.ningshang.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long>, JpaSpecificationExecutor<News> {
    List<News> findAllByOrderByIdDesc();
    List<News> findByCategoryOrderByIdDesc(String category);
    News findByNewsId(String newsId);
}
