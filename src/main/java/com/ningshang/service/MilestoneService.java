package com.ningshang.service;

import com.ningshang.entity.Milestone;
import com.ningshang.repository.MilestoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ningshang.exception.BusinessException;
import com.ningshang.util.RichText;
import java.util.List;

@Service
public class MilestoneService {
    @Autowired
    private MilestoneRepository milestoneRepository;
    
    public List<Milestone> findAll() {
        return milestoneRepository.findAllByOrderBySortOrderAscIdAsc();
    }

    public Milestone save(Milestone milestone) {
        if (milestone.getId() != null) {
            Milestone existing = milestoneRepository.findById(milestone.getId()).orElseThrow(() -> new BusinessException(404, "记录不存在"));
            milestone.setCreatedAt(existing.getCreatedAt());
        }
        milestone.setDescription(RichText.clean(milestone.getDescription()));
        return milestoneRepository.save(milestone);
    }

    public void delete(Long id) {
        milestoneRepository.deleteById(id);
    }
}