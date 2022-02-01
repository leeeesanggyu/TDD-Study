package com.tddstudy.membership.dto;

import com.tddstudy.membership.util.MembershipKindType;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class MembershipRes {

    private final Long id;
    private final MembershipKindType kind;
}
