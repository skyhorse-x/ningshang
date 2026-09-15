package com.ningshang.repository;

import com.ningshang.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    List<TeamMember> findAllByOrderBySortOrderAscIdAsc();
    List<TeamMember> findAllByOrderBySortOrderAsc();
    List<TeamMember> findByNameContainingOrderByCreatedAtDesc(String name);
}
