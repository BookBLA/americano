package com.bookbla.americano.domain.member.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.member.controller.dto.request.MemberPolicyCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberPolicyUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberPolicyResponse;
import com.bookbla.americano.domain.member.service.MemberPolicyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    @Operation(summary = "사용자 동의 약관 생성 API",
        description = "해당 멤버의 약관 동의를 생성")
    @PostMapping
    public ResponseEntity<MemberPolicyResponse> createMemberPolicies(
        @Parameter(hidden = true) @User LoginUser loginUser,
        @RequestBody MemberPolicyCreateRequest memberPolicyCreateRequest
    ) {
        MemberPolicyResponse memberPolicyResponse = memberPolicyService.createMemberPolicies(
            loginUser.getMemberId(), memberPolicyCreateRequest.toDto());
        return ResponseEntity.ok(memberPolicyResponse);
    }

    @Operation(summary = "사용자 동의 약관 조회 API",
        description = "해당 사용자의 약관 동의를 확인")
    @GetMapping
    public ResponseEntity<MemberPolicyResponse> readMemberPolicies(
        @Parameter(hidden = true) @User LoginUser loginUser) {
        MemberPolicyResponse memberPolicyResponse = memberPolicyService.readMemberPolicies(
            loginUser.getMemberId());
        return ResponseEntity.ok(memberPolicyResponse);
    }

    @Operation(summary = "사용자 동의 약관 수정 API",
        description = "해당 사용자의 약관 동의를 수정")
    @PutMapping
    public ResponseEntity<MemberPolicyResponse> updateMemberPolicies(
        @Parameter(hidden = true) @User LoginUser loginUser,
        @RequestBody @Valid MemberPolicyUpdateRequest memberPolicyUpdateRequests
    ) {
        MemberPolicyResponse memberPolicyResponse = memberPolicyService.updateMemberPolicies(
            loginUser.getMemberId(), memberPolicyUpdateRequests);
        return ResponseEntity.ok(memberPolicyResponse);
    }

}
