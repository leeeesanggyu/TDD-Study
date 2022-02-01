package com.tddstudy.membership;

import com.tddstudy.membership.entity.Membership;
import com.tddstudy.membership.util.MembershipKindType;
import com.tddstudy.membership.repo.MembershipRepo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Slf4j
public class MembershipRepoTest {

    @Autowired
    private MembershipRepo membershipRepo;

    @Test
    public void MembershipRepoCheckNull() {
        assertThat(membershipRepo).isNotNull();
    }

    @Test
    public void 멤버쉽가입() {
        final Membership membership = Membership.builder()
                .userId("salgu")
                .kind(MembershipKindType.KAKAO)
                .point(1000)
                .build();

        Membership result = membershipRepo.save(membership);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getUserId()).isEqualTo("salgu");
        assertThat(result.getKind()).isEqualTo(MembershipKindType.KAKAO);
        assertThat(result.getPoint()).isEqualTo(1000);
    }

    @Test
    public void 멤버쉽조회() {
        final Membership membership = Membership.builder()
                .userId("salgu")
                .kind(MembershipKindType.KAKAO)
                .point(1000)
                .build();

        membershipRepo.save(membership);
        Membership result = membershipRepo.findByUserIdAndKind(membership.getUserId(), membership.getKind());

        assertThat(result.getId()).isNotNull();
        assertThat(result.getUserId()).isEqualTo("salgu");
        assertThat(result.getKind()).isEqualTo(MembershipKindType.KAKAO);
        assertThat(result.getPoint()).isEqualTo(1000);

    }
}
