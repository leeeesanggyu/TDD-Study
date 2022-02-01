package com.tddstudy.membership.dto;

import com.tddstudy.membership.util.MembershipKindType;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class MembershipReq {

    @NotNull @Min(0)
    private final Integer point;

    @NotNull
    private final MembershipKindType kind;
}
