package com.ningshang.service;

import com.ningshang.entity.Job;
import com.ningshang.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ningshang.exception.BusinessException;
import com.ningshang.util.RichText;
import java.util.List;

@Service
public class JobService {
    @Autowired
    private JobRepository jobRepository;
    
    public List<Job> findAll() {
        return jobRepository.findAllByOrderBySortOrderAscIdAsc();
    }

    public Job save(Job job) {
        if (job.getId() != null) {
            Job existing = jobRepository.findById(job.getId()).orElseThrow(() -> new BusinessException(404, "记录不存在"));
            job.setCreatedAt(existing.getCreatedAt());
        }
        job.setDescription(RichText.clean(job.getDescription()));
        return jobRepository.save(job);
    }

    public void delete(Long id) {
        jobRepository.deleteById(id);
    }
}