package com.tddstudy.membership.service;

import com.tddstudy.membership.dto.MembershipDetailRes;
import com.tddstudy.membership.dto.MembershipRes;
import com.tddstudy.membership.entity.Membership;
import com.tddstudy.membership.util.MembershipKindType;
import com.tddstudy.exception.MembershipErrorResult;
import com.tddstudy.exception.MembershipException;
import com.tddstudy.membership.repo.MembershipRepo;
import com.tddstudy.point.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MembershipService {

    private final MembershipRepo membershipRepo;
    private final PointService ratePointService;

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

    public List<MembershipDetailRes> getMembershipList(final String userId) {
        final List<Membership> membershipList = membershipRepo.findAllByUserId(userId);

        return membershipList.stream().map(v -> MembershipDetailRes.builder()
                .id(v.getId())
                .userId(v.getUserId())
                .kind(v.getKind())
                .point(v.getPoint())
                .build())
                .collect(Collectors.toList());
    }

    public MembershipDetailRes getMembershipDetail(final String userId, final MembershipKindType kindType) {
        final Membership result = membershipRepo.findByUserIdAndKind(userId, kindType);

        if (result == null) {
            throw new MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND);
        }

        return MembershipDetailRes.builder()
                .id(result.getId())
                .userId(result.getUserId())
                .kind(result.getKind())
                .point(result.getPoint())
                .build();
    }

    public void deleteMembership(final String userId, final Long membershipId) {
        final Membership membership = membershipRepo.findById(membershipId)
                .orElseThrow(() -> new MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND));

        if(membership.getUserId() == userId) {
            membershipRepo.deleteById(membershipId);
        }
        else {
            throw new MembershipException(MembershipErrorResult.NOT_MEMBERSHIP_OWNER);
        }
    }

    public void accumulateMembershipPoint(final String userId, final Long membershipId, final int amount) {
        final Optional<Membership> optionalMembership = membershipRepo.findById(membershipId);
        final Membership membership = optionalMembership.orElseThrow(
                () -> new MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND)
        );

        if (!membership.getUserId().equals(userId)) {
            throw new MembershipException(MembershipErrorResult.NOT_MEMBERSHIP_OWNER);
        }

        final int additionalAmount = ratePointService.calculate(amount);
        membership.setPoint(additionalAmount + membership.getPoint());
    }

}
