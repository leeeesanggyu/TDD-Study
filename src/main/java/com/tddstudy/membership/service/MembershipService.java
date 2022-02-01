package com.tddstudy.membership.service;

import com.tddstudy.membership.dto.MembershipRes;
import com.tddstudy.membership.entity.Membership;
import com.tddstudy.membership.util.MembershipKindType;
import com.tddstudy.membership.exception.MembershipErrorResult;
import com.tddstudy.membership.exception.MembershipException;
import com.tddstudy.membership.repo.MembershipRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MembershipService {

    private final MembershipRepo membershipRepo;

    public MembershipRes addMembership(final String userId, final MembershipKindType kindType, final Integer point) {
        final Membership result = membershipRepo.findByUserIdAndKind(userId, kindType);
        if (result != null) {
            throw new MembershipException(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER);
        }

        final Membership membership = Membership.builder()
                .userId(userId)
                .point(point)
                .kind(kindType)
                .build();

        Membership saveResult = membershipRepo.save(membership);

        return MembershipRes.builder()
                .id(saveResult.getId())
                .kind(saveResult.getKind())
                .build();
    }
}
