package com.ningshang.service;

import com.ningshang.entity.NewsCategory;
import com.ningshang.repository.NewsCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsCategoryService {
    @Autowired
    private NewsCategoryRepository newsCategoryRepository;

    public List<NewsCategory> findAll() {
        return newsCategoryRepository.findAllByOrderBySortOrderAsc();
    }

    public NewsCategory save(NewsCategory category) {
        return newsCategoryRepository.save(category);
    }

    public void delete(Long id) {
        newsCategoryRepository.deleteById(id);
    }

    public boolean existsByKey(String key) {
        return newsCategoryRepository.existsByKey(key);
    }
}
