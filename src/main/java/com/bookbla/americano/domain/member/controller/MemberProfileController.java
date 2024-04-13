package com.bookbla.americano.domain.member.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.member.controller.dto.request.MemberProfileCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberProfileStatusUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberProfileUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberProfileResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberProfileStatusResponse;
import com.bookbla.americano.domain.member.service.MemberProfileService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/member-profiles")
@RequiredArgsConstructor
public class MemberProfileController {

    private final MemberProfileService memberProfileService;

    @PostMapping
    public ResponseEntity<MemberProfileResponse> createMemberProfile(
        @Parameter(hidden = true) @User LoginUser loginUser,
        @RequestBody @Valid MemberProfileCreateRequest memberProfileCreateRequest) {
        MemberProfileResponse memberProfileResponse = memberProfileService.createMemberProfile(
            loginUser.getMemberId(), memberProfileCreateRequest.toMemberProfileDto());

        return ResponseEntity.ok(memberProfileResponse);
    }

    @GetMapping
    public ResponseEntity<MemberProfileResponse> readMemberProfile(
        @Parameter(hidden = true) @User LoginUser loginUser) {
        MemberProfileResponse memberProfileResponse = memberProfileService.readMemberProfile(
            loginUser.getMemberId());
        return ResponseEntity.ok(memberProfileResponse);
    }

    @PutMapping
    public ResponseEntity<MemberProfileResponse> updateMemberProfile(
        @Parameter(hidden = true) @User LoginUser loginUser,
        @RequestBody @Valid MemberProfileUpdateRequest memberProfileUpdateRequest
    ) {
        MemberProfileResponse memberProfileResponse = memberProfileService.updateMemberProfile(
            loginUser.getMemberId(), memberProfileUpdateRequest);
        return ResponseEntity.ok(memberProfileResponse);
    }

    @GetMapping("/statuses")
    public ResponseEntity<MemberProfileStatusResponse> readMemberProfileStatus(
        @Parameter(hidden = true) @User LoginUser loginUser) {
        MemberProfileStatusResponse memberProfileStatusResponse = memberProfileService.readMemberProfileStatus(
            loginUser.getMemberId());

        return ResponseEntity.ok(memberProfileStatusResponse);
    }

    @PatchMapping("/statuses")
    public ResponseEntity<MemberProfileStatusResponse> updateMemberProfileStatus(
        @Parameter(hidden = true) @User LoginUser loginUser,
        @RequestBody @Valid MemberProfileStatusUpdateRequest memberProfileStatusUpdateRequest) {
        MemberProfileStatusResponse memberProfileStatusResponse = memberProfileService.updateMemberProfileStatus(
            loginUser.getMemberId(), memberProfileStatusUpdateRequest.toDto());

        return ResponseEntity.ok(memberProfileStatusResponse);
    }

}
