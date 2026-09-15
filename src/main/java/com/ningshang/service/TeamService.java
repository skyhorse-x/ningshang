package com.ningshang.service;

import com.ningshang.entity.TeamMember;
import com.ningshang.repository.TeamMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ningshang.exception.BusinessException;
import com.ningshang.util.RichText;
import java.util.List;

@Service
public class TeamService {
    @Autowired
    private TeamMemberRepository teamMemberRepository;
    
    public List<TeamMember> findAll() {
        return teamMemberRepository.findAllByOrderBySortOrderAscIdAsc();
    }

    public TeamMember save(TeamMember member) {
        if (member.getId() != null) {
            TeamMember existing = teamMemberRepository.findById(member.getId()).orElseThrow(() -> new BusinessException(404, "记录不存在"));
            member.setCreatedAt(existing.getCreatedAt());
        }
        member.setDescription(RichText.clean(member.getDescription()));
        return teamMemberRepository.save(member);
    }

    public void delete(Long id) {
        teamMemberRepository.deleteById(id);
    }
}