package com.tddstudy.membership.repo;

import com.tddstudy.membership.entity.Membership;
import com.tddstudy.membership.util.MembershipKindType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MembershipRepo extends JpaRepository<Membership, Long> {
    Membership findByUserIdAndKind(String userId, MembershipKindType kind);
    Membership save(Membership membership);
    List<Membership> findAllByUserId(String userId);
}
