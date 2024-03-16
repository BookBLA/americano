package com.bookbla.americano.domain.member.controller;

import com.bookbla.americano.base.jwt.LoginUser;
import com.bookbla.americano.domain.member.controller.dto.request.MemberPolicyCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberPolicyUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberPolicyResponse;
import com.bookbla.americano.domain.member.service.MemberPolicyService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/members/policies")
@RequiredArgsConstructor
public class MemberPolicyController {

    private final MemberPolicyService memberPolicyService;

    @PostMapping
    public ResponseEntity<MemberPolicyResponse> createMemberPolicies(@LoginUser Long memberId,
        @RequestBody MemberPolicyCreateRequest memberPolicyCreateRequest) {
        MemberPolicyResponse memberPolicyResponse = memberPolicyService.createMemberPolicies(
            memberId, memberPolicyCreateRequest.toDto());

        return ResponseEntity.ok(memberPolicyResponse);
    }

    @GetMapping
    public ResponseEntity<MemberPolicyResponse> readMemberPolicies(@LoginUser Long memberId) {

        MemberPolicyResponse memberPolicyResponse = memberPolicyService.readMemberPolicies(
            memberId);

        return ResponseEntity.ok(memberPolicyResponse);
    }


    @PutMapping
    public ResponseEntity<MemberPolicyResponse> updateMemberPolicies(
        @RequestBody @Valid MemberPolicyUpdateRequest memberPolicyUpdateRequests,
        @LoginUser Long memberId) {

        MemberPolicyResponse memberPolicyResponse = memberPolicyService.updateMemberPolicies(
            memberId, memberPolicyUpdateRequests);

        return ResponseEntity.ok(memberPolicyResponse);
    }

}
