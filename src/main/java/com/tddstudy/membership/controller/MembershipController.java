package com.tddstudy.membership.controller;

import com.tddstudy.membership.dto.MembershipDetailRes;
import com.tddstudy.membership.dto.MembershipReq;
import com.tddstudy.membership.dto.MembershipRes;
import com.tddstudy.membership.exception.GlobalExceptionHandler;
import com.tddstudy.membership.service.MembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static com.tddstudy.membership.util.MembershipConstants.USER_ID_HEADER;

@RestController
@RequiredArgsConstructor
public class MembershipController extends GlobalExceptionHandler{

    private final MembershipService membershipService;

    @PostMapping("/api/v1/membership")
    public ResponseEntity<MembershipRes> addMembership (
            @RequestHeader(USER_ID_HEADER) final String userId,
            @RequestBody @Valid final MembershipReq membershipReq
    ) {
        MembershipRes membershipRes = membershipService.addMembership(userId, membershipReq.getKind(), membershipReq.getPoint());
        return ResponseEntity.status(HttpStatus.CREATED).body(membershipRes);
    }

    @PostMapping("/api/v1/membership/list")
    public ResponseEntity<List<MembershipDetailRes>> getMembershipList (
            @RequestHeader(USER_ID_HEADER) final String userId
    ) {
        return ResponseEntity.ok(membershipService.getMembershipList(userId));
    }
}


