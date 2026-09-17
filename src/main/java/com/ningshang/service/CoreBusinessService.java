package com.ningshang.service;

import com.ningshang.entity.CoreBusiness;
import com.ningshang.exception.BusinessException;
import com.ningshang.repository.CoreBusinessRepository;
import com.ningshang.util.RichText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
