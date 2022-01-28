package com.tddstudy.membership;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepo extends JpaRepository<Membership, Long> {
    Membership findByUserIdAndKind(String userId, MembershipKindType kind);
}
