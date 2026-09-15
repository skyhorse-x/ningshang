package com.ningshang.service;

import com.ningshang.entity.Honor;
import com.ningshang.repository.HonorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ningshang.exception.BusinessException;
import com.ningshang.util.RichText;
import java.util.List;

@Service
public class HonorService {
    @Autowired
    private HonorRepository honorRepository;
    
    public List<Honor> findAll() {
        return honorRepository.findAllByOrderBySortOrderAscIdAsc();
    }

    public Honor save(Honor honor) {
        if (honor.getId() != null) {
            Honor existing = honorRepository.findById(honor.getId()).orElseThrow(() -> new BusinessException(404, "记录不存在"));
            honor.setCreatedAt(existing.getCreatedAt());
        }
        honor.setDescription(RichText.clean(honor.getDescription()));
        return honorRepository.save(honor);
    }

    public void delete(Long id) {
        honorRepository.deleteById(id);
    }
}