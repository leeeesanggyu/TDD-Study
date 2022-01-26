package com.tddstudy.membership;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class MembershipRepoTest {

    @Autowired
    private MembershipRepo membershipRepo;

    @Test
    public void MembershipRepoCheckNull() {
        assertThat(membershipRepo).isNotNull();
    }

    @Test
    public void registerMembership() {
        Membership membership = Membership.builder()
                .userId("salgu")
                .kind("naver")
                .point(1000)
                .build();

        Membership result = membershipRepo.save(membership);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getUserId()).isEqualTo("salgu");
        assertThat(result.getKind()).isEqualTo("naver");
        assertThat(result.getPoint()).isEqualTo(1000);
    }
}
