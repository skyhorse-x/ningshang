package com.ningshang.service;

import com.ningshang.entity.SiteContent;
import com.ningshang.repository.SiteContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ningshang.exception.BusinessException;
import com.ningshang.util.RichText;
import java.util.List;

@Service
public class SiteContentService {
    @Autowired
    private SiteContentRepository siteContentRepository;

    public List<SiteContent> findAll() {
        return siteContentRepository.findAllByOrderBySortOrderAsc();
    }

    public SiteContent save(SiteContent sc) {
        if (sc.getId() != null) {
            SiteContent existing = siteContentRepository.findById(sc.getId()).orElseThrow(() -> new BusinessException(404, "记录不存在"));
            
        }
        if (RichText.isContentKey(sc.getContentKey())) sc.setContent(RichText.clean(sc.getContent()));
        return siteContentRepository.save(sc);
    }

    public void delete(Long id) {
        siteContentRepository.deleteById(id);
    }
}
