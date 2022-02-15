package com.tddstudy.membership;

import com.tddstudy.membership.dto.MembershipDetailRes;
import com.tddstudy.membership.dto.MembershipRes;
import com.tddstudy.membership.entity.Membership;
import com.tddstudy.membership.util.MembershipKindType;
import com.tddstudy.exception.MembershipErrorResult;
import com.tddstudy.exception.MembershipException;
import com.tddstudy.membership.repo.MembershipRepo;
import com.tddstudy.membership.service.MembershipService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MembershipServiceTest {

    @InjectMocks
    private MembershipService target;
    @Mock
    private MembershipRepo membershipRepo;

    private final String userId = "userId";
    private final MembershipKindType kindType = MembershipKindType.NAVER;
    private final Integer point = 10000;
    private final Long membershipId = -1L;

    private Membership membership() {
        return Membership.builder()
                .id(-1L)
                .userId(userId)
                .point(point)
                .kind(MembershipKindType.NAVER)
                .build();
    }

    @Test
    public void 멤버십등록실패_이미존재함() {
        // given
        // membershipRepo.findByUserIdAndKind() 했을때 Membership객체를 반환합니다.
        doReturn(Membership.builder().build()).when(membershipRepo).findByUserIdAndKind(userId, kindType);

        // when
        final MembershipException result = assertThrows(
                MembershipException.class,  // 명시적으로 해줘야 하는듯
                () -> target.addMembership(userId, kindType, point)
        );

        // then
        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER);
    }

    @Test
    public void 멤버쉽등록성공() {
        //given
        doReturn(null).when(membershipRepo).findByUserIdAndKind(userId, kindType);
        doReturn(membership()).when(membershipRepo).save(any(Membership.class));

        //when
        final MembershipRes result = target.addMembership(userId, kindType, point);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getKind()).isEqualTo(MembershipKindType.NAVER);

        // verify
        verify(membershipRepo, times(1)).findByUserIdAndKind(userId, kindType);
        verify(membershipRepo, times(1)).save(any(Membership.class));

    }

    @Test
    public void 멤버쉽목록조회() {
        doReturn(Arrays.asList(
                Membership.builder().build(),
                Membership.builder().build(),
                Membership.builder().build()
        )).when(membershipRepo).findAllByUserId(userId);

        final List<MembershipDetailRes> result = target.getMembershipList(userId);

        assertThat(result.size()).isEqualTo(3);
    }

    @Test
    public void 멤버쉽상세조회실패_null() {
        doReturn(null)
                .when(membershipRepo)
                .findByUserIdAndKind(userId, kindType);

        final MembershipException result = assertThrows(
                MembershipException.class,
                () -> target.getMembershipDetail(userId, kindType)
        );

        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.MEMBERSHIP_NOT_FOUND);
    }

    @Test
    public void 멤버쉽상세조회성공() {
        doReturn(membership())
                .when(membershipRepo)
                .findByUserIdAndKind(userId, kindType);

        MembershipDetailRes result = target.getMembershipDetail(userId, kindType);

        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getKind()).isEqualTo(kindType);
    }

    @Test
    public void 멤버쉽삭제실패_없음() {
        doReturn(Optional.empty())
                .when(membershipRepo).findById(membershipId);

        final MembershipException result = assertThrows(
                MembershipException.class,
                () -> target.deleteMembership(userId, membershipId)
        );

        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.MEMBERSHIP_NOT_FOUND);
    }

    @Test
    public void 멤버쉽삭제실패_본인아님() {
        final Membership membership = membership();
        doReturn(Optional.ofNullable(membership))
                .when(membershipRepo).findById(membershipId);

        final MembershipException result = assertThrows(
                MembershipException.class,
                () -> target.deleteMembership("no", membershipId)
        );

        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.NOT_MEMBERSHIP_OWNER);
    }

    @Test
    public void 멤버쉽삭제성공() {
        final Membership membership = membership();
        doReturn(Optional.ofNullable(membership))
                .when(membershipRepo).findById(membershipId);

        target.deleteMembership(userId, membershipId);
    }
}
