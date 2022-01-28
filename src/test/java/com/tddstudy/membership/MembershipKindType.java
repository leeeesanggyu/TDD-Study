package com.tddstudy.membership;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MembershipKindType {
    NAVER("네이버"),
    DAUM("다음"),
    KAKAO("카카오")
    ;

    private final String MembershipKind;

}
