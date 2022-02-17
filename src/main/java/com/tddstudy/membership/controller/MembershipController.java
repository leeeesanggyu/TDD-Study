package com.tddstudy.membership.controller;

import com.tddstudy.membership.dto.MembershipDetailRes;
import com.tddstudy.membership.dto.MembershipReq;
import com.tddstudy.membership.dto.MembershipRes;
import com.tddstudy.exception.GlobalExceptionHandler;
import com.tddstudy.membership.service.MembershipService;
import com.tddstudy.membership.util.MembershipKindType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/api/v1/membership/list")
    public ResponseEntity<List<MembershipDetailRes>> getMembershipList (
            @RequestHeader(USER_ID_HEADER) final String userId
    ) {
        return ResponseEntity.ok(membershipService.getMembershipList(userId));
    }

    @GetMapping("/api/v1/membership")
    public ResponseEntity<MembershipDetailRes> getMembershipDetail (
            @RequestHeader(USER_ID_HEADER) final String userId,
            @RequestParam final MembershipKindType kindType
    ) {
        return ResponseEntity.ok().body(membershipService.getMembershipDetail(userId, kindType));
    }

    @DeleteMapping("/api/v1/membership/{membershipId}")
    public ResponseEntity<MembershipDetailRes> DeleteMembership (
            @RequestHeader(USER_ID_HEADER) final String userId,
            @PathVariable final Long membershipId
    ) {
        membershipService.deleteMembership(userId, membershipId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/v1/membership/{id}/accumulate")
    public ResponseEntity<Void> accumulateMembershipPoint(
            @RequestHeader(USER_ID_HEADER) final String userId,
            @PathVariable final Long id,
            @RequestBody @Valid final MembershipReq membershipReq
    ) {
        membershipService.accumulateMembershipPoint(userId, id, membershipReq.getPoint());
        return ResponseEntity.noContent().build();
    }

}


