package com.tddstudy.membership.dto;

import com.tddstudy.membership.util.MembershipKindType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MembershipDetailRes {

    private Long id;
    private String userId;
    private Integer point;
    private MembershipKindType kind;
}
