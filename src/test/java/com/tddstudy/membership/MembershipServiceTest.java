package com.tddstudy.membership;

import com.tddstudy.membership.dto.MembershipRes;
import com.tddstudy.membership.entity.Membership;
import com.tddstudy.membership.entity.MembershipKindType;
import com.tddstudy.membership.exception.MembershipErrorResult;
import com.tddstudy.membership.exception.MembershipException;
import com.tddstudy.membership.repo.MembershipRepo;
import com.tddstudy.membership.service.MembershipService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    private Membership membership() {
        return Membership.builder()
                .id(-1L)
                .userId(userId)
                .point(point)
                .kind(MembershipKindType.KAKAO)
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
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getKind()).isEqualTo(MembershipKindType.KAKAO);
        assertThat(result.getPoint()).isEqualTo(10000);

        // verify
        verify(membershipRepo, times(1)).findByUserIdAndKind(userId, kindType);
        verify(membershipRepo, times(1)).save(any(Membership.class));

    }
}
