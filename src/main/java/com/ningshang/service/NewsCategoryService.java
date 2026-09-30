package com.ningshang.service;

import com.ningshang.entity.NewsCategory;
import com.ningshang.exception.BusinessException;
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
        if (category.getKey() == null || category.getKey().trim().isEmpty()) {
            throw new BusinessException(400, "分类标识不能为空");
        }
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new BusinessException(400, "分类名称不能为空");
        }
        category.setKey(category.getKey().trim());
        category.setName(category.getName().trim());
        if (category.getSortOrder() == null) category.setSortOrder(1);
        if (category.getIsActive() == null) category.setIsActive(true);
        newsCategoryRepository.findByKey(category.getKey()).ifPresent(existing -> {
            if (category.getId() == null || !existing.getId().equals(category.getId())) {
                throw new BusinessException(409, "分类标识已存在");
            }
        });
        return newsCategoryRepository.save(category);
    }

    public void delete(Long id) {
        if (!newsCategoryRepository.existsById(id)) {
            throw new BusinessException(404, "新闻分类不存在");
        }
        newsCategoryRepository.deleteById(id);
    }

    public boolean existsByKey(String key) {
        return newsCategoryRepository.existsByKey(key);
    }
}
