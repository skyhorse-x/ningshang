package com.ningshang.service;

import com.ningshang.entity.CoreBusiness;
import com.ningshang.exception.BusinessException;
import com.ningshang.repository.CoreBusinessRepository;
import com.ningshang.util.RichText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CoreBusinessService {
    @Autowired
    private CoreBusinessRepository coreBusinessRepository;

    public List<CoreBusiness> findAll() {
        return coreBusinessRepository.findAllByOrderBySortOrderAscIdAsc();
    }

    public CoreBusiness save(CoreBusiness coreBusiness) {
        if (coreBusiness.getId() != null) {
            CoreBusiness existing = coreBusinessRepository.findById(coreBusiness.getId())
                    .orElseThrow(() -> new BusinessException(404, "记录不存在"));
            coreBusiness.setCreatedAt(existing.getCreatedAt());
        }
        coreBusiness.setDescription(RichText.clean(coreBusiness.getDescription()));
        return coreBusinessRepository.save(coreBusiness);
    }

    public void delete(Long id) {
        coreBusinessRepository.deleteById(id);
    }

    /**
     * 按成员企业填的「所属领域」文字，在核心业务领域表里找最贴近的一项。
     *
     * 匹配顺序：完全相同 → 互相包含 → 中文双字（bigram）重合度最高。
     * 分数相同时保留先遇到的（列表已按 sortOrder 排序），因此结果稳定可预期。
     * 一个都匹配不到时返回 null，由调用方决定是否隐藏该板块。
     */
    public CoreBusiness matchByCategory(String category, List<CoreBusiness> candidates) {
        if (candidates == null || candidates.isEmpty()) return null;
        String target = normalize(category);
        if (target.isEmpty()) return null;

        CoreBusiness best = null;
        double bestScore = 0;
        for (CoreBusiness item : candidates) {
            if (item == null || item.getName() == null) continue;
            double score = score(target, normalize(item.getName()));
            if (score > bestScore) {
                bestScore = score;
                best = item;
            }
        }
        return bestScore >= 1 ? best : null;
    }

    /** 只保留中文、字母、数字，抹掉空格与各种标点，避免"（一期）"之类的写法干扰匹配。 */
    private static String normalize(String value) {
        return value == null ? "" : value.replaceAll("[^\\u4e00-\\u9fa5a-zA-Z0-9]", "").toLowerCase();
    }

    private static double score(String target, String name) {
        if (target.isEmpty() || name.isEmpty()) return 0;
        if (target.equals(name)) return 1000;
        if (target.contains(name) || name.contains(target)) return 500;
        Set<String> targetPairs = bigrams(target);
        Set<String> namePairs = bigrams(name);
        int hit = 0;
        for (String pair : targetPairs) {
            if (namePairs.contains(pair)) hit++;
        }
        return hit;
    }

    private static Set<String> bigrams(String value) {
        Set<String> pairs = new HashSet<>();
        for (int i = 0; i + 2 <= value.length(); i++) {
            pairs.add(value.substring(i, i + 2));
        }
        return pairs;
    }
}
