package com.tddstudy.membership.dto;

import com.tddstudy.membership.entity.MembershipKindType;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
public class MembershipRes {

    private Long id;
    private String userId;
    private MembershipKindType kind;
    private Integer point;
}
