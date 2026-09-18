package com.ningshang.service;

import com.ningshang.entity.Partner;
import com.ningshang.exception.BusinessException;
import com.ningshang.repository.PartnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartnerService {
    @Autowired
    private PartnerRepository partnerRepository;

    public List<Partner> findAll() {
        return partnerRepository.findAllByOrderBySortOrderAscIdAsc();
    }

    public List<Partner> findEnabled() {
        return partnerRepository.findByStatusOrderBySortOrderAscIdAsc(1);
    }

    public Partner save(Partner partner) {
        if (partner.getId() != null) {
            Partner existing = partnerRepository.findById(partner.getId())
                    .orElseThrow(() -> new BusinessException(404, "记录不存在"));
            partner.setCreatedAt(existing.getCreatedAt());
        }
        if (partner.getStatus() == null) partner.setStatus(1);
        return partnerRepository.save(partner);
    }

    public void delete(Long id) {
        if (!partnerRepository.existsById(id)) throw new BusinessException(404, "记录不存在");
        partnerRepository.deleteById(id);
    }
}
