package com.bookbla.americano.domain.member.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.member.controller.dto.request.MemberPolicyCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberPolicyUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberPolicyResponse;
import com.bookbla.americano.domain.member.service.MemberPolicyService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<MemberPolicyResponse> createMemberPolicies(
            @User LoginUser loginUser,
            @RequestBody MemberPolicyCreateRequest memberPolicyCreateRequest
    ) {
        MemberPolicyResponse memberPolicyResponse = memberPolicyService.createMemberPolicies(loginUser.getMemberId(), memberPolicyCreateRequest.toDto());
        return ResponseEntity.ok(memberPolicyResponse);
    }

    @GetMapping
    public ResponseEntity<MemberPolicyResponse> readMemberPolicies(@User LoginUser loginUser) {
        MemberPolicyResponse memberPolicyResponse = memberPolicyService.readMemberPolicies(loginUser.getMemberId());
        return ResponseEntity.ok(memberPolicyResponse);
    }

    @PutMapping
    public ResponseEntity<MemberPolicyResponse> updateMemberPolicies(
        @RequestBody @Valid MemberPolicyUpdateRequest memberPolicyUpdateRequests,
        @User LoginUser loginUser
    ) {
        MemberPolicyResponse memberPolicyResponse = memberPolicyService.updateMemberPolicies(loginUser.getMemberId(), memberPolicyUpdateRequests);
        return ResponseEntity.ok(memberPolicyResponse);
    }

}
