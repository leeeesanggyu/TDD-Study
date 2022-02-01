package com.tddstudy.membership.controller;

import com.tddstudy.membership.dto.MembershipReq;
import com.tddstudy.membership.dto.MembershipRes;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.tddstudy.membership.MembershipConstants.USER_ID_HEADER;

@RestController
public class MembershipController {

    @PostMapping("/api/v1/membership")
    public ResponseEntity<MembershipRes> addMembership(
            @RequestHeader(USER_ID_HEADER) final String userId,
            @RequestBody @Valid final MembershipReq membershipReq
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
