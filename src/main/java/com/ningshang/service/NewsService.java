package com.ningshang.service;

import com.ningshang.entity.News;
import com.ningshang.repository.NewsRepository;
import com.ningshang.util.RichText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ningshang.exception.BusinessException;
import java.util.List;
import com.ningshang.dto.PageResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;

@Service
public class NewsService {
    @Autowired
    private NewsRepository newsRepository;


    public List<News> findAll() {
        return newsRepository.findAllByOrderByIdDesc();
    }

    public List<News> findByCategory(String category) {
        return newsRepository.findByCategoryOrderByIdDesc(category);
    }

    public PageResult<News> findAdminPage(int page, int size, String title, String category) {
        int safePage = Math.max(page, 1);
        int safeSize = Math.min(Math.max(size, 1), 100);
        String keyword = title == null ? "" : title.trim();
        String categoryValue = category == null ? "" : category.trim();
        Specification<News> filters = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!keyword.isEmpty()) predicates.add(builder.like(root.get("title"), "%" + keyword + "%"));
            if (!categoryValue.isEmpty()) predicates.add(builder.equal(root.get("category"), categoryValue));
            return builder.and(predicates.toArray(new Predicate[0]));
        };
        Page<News> result = newsRepository.findAll(filters,
                PageRequest.of(safePage - 1, safeSize, Sort.by(Sort.Direction.DESC, "id")));
        return new PageResult<>(result.getContent(), result.getTotalElements(), safePage, safeSize);
    }

    public News findByNewsId(String newsId) {
        return newsRepository.findByNewsId(newsId);
    }

    public News save(News news) {
        if (news.getId() != null) {
            News existing = newsRepository.findById(news.getId()).orElseThrow(() -> new BusinessException(404, "记录不存在"));
            news.setCreatedAt(existing.getCreatedAt());
        }
        if (news.getNewsId() == null || news.getNewsId().trim().isEmpty()) {
            news.setNewsId("news-" + java.util.UUID.randomUUID());
        }
        News same = newsRepository.findByNewsId(news.getNewsId());
        if (same != null && !same.getId().equals(news.getId())) throw new BusinessException(409, "新闻标识已存在");
        // 入库前清洗富文本，防存储型 XSS
        if (news.getBody() != null) {
            news.setBody(RichText.clean(news.getBody()));
        }
        return newsRepository.save(news);
    }

    public void delete(Long id) {
        newsRepository.deleteById(id);
    }
}
