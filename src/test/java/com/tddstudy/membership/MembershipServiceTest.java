package com.tddstudy.membership;

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

import static org.mockito.Mockito.doReturn;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class MembershipServiceTest {

    @InjectMocks
    private MembershipService target;
    @Mock
    private MembershipRepo membershipRepo;

    private final String userId = "userId";
    private final MembershipKindType membershipType = MembershipKindType.NAVER;
    private final Integer point = 10000;

    @Test
    public void 멤버십등록실패_이미존재함() {
        // given
        doReturn(Membership.builder().build()).when(membershipRepo).findByUserIdAndKind(userId, membershipType);

        // when
        final MembershipException result = assertThrows(
                MembershipException.class,
                () -> target.addMembership(userId, membershipType, point)
        );

        // then
        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER);
    }
}
