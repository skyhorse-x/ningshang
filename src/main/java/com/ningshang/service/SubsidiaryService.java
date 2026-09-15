package com.ningshang.service;

import com.ningshang.entity.Subsidiary;
import com.ningshang.repository.SubsidiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ningshang.exception.BusinessException;
import com.ningshang.util.RichText;
import java.util.List;

@Service
public class SubsidiaryService {
    @Autowired
    private SubsidiaryRepository subsidiaryRepository;
    
    public List<Subsidiary> findAll() {
        return subsidiaryRepository.findAllByOrderBySortOrderAscIdAsc();
    }

    public Subsidiary save(Subsidiary subsidiary) {
        if (subsidiary.getId() != null) {
            Subsidiary existing = subsidiaryRepository.findById(subsidiary.getId()).orElseThrow(() -> new BusinessException(404, "记录不存在"));
            subsidiary.setCreatedAt(existing.getCreatedAt());
        }
        subsidiary.setDescription(RichText.clean(subsidiary.getDescription()));
        return subsidiaryRepository.save(subsidiary);
    }

    public void delete(Long id) {
        subsidiaryRepository.deleteById(id);
    }
}