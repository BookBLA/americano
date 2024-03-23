package com.bookbla.americano.domain.member.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.member.controller.dto.request.MemberProfileCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberProfileUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberProfileResponse;
import com.bookbla.americano.domain.member.service.MemberProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
            @User LoginUser loginUser,
            @RequestBody @Valid MemberProfileCreateRequest memberProfileCreateRequest
    ) {
        MemberProfileResponse memberProfileResponse = memberProfileService.createMemberProfile(loginUser.getMemberId(), memberProfileCreateRequest.toDto());
        return ResponseEntity.ok(memberProfileResponse);
    }

    @GetMapping
    public ResponseEntity<MemberProfileResponse> readMemberProfile(@User LoginUser loginUser) {
        MemberProfileResponse memberProfileResponse = memberProfileService.readMemberProfile(loginUser.getMemberId());
        return ResponseEntity.ok(memberProfileResponse);
    }

    @PutMapping
    public ResponseEntity<MemberProfileResponse> updateMemberProfile(
        @RequestBody @Valid MemberProfileUpdateRequest memberProfileUpdateRequest,
        @User LoginUser loginUser
    ) {
        MemberProfileResponse memberProfileResponse = memberProfileService.updateMemberProfile(loginUser.getMemberId(), memberProfileUpdateRequest);
        return ResponseEntity.ok(memberProfileResponse);
    }
}
