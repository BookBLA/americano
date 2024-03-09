package com.bookbla.americano.domain.member.controller;

import com.bookbla.americano.base.jwt.LoginUser;
import com.bookbla.americano.domain.member.controller.dto.request.MemberProfileCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberProfileUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberProfileResponse;
import com.bookbla.americano.domain.member.service.MemberProfileService;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBookProfileResponseDto;
import com.bookbla.americano.domain.member.controller.dto.response.MemberProfileCreateResponse;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.service.MemberProfileService;
import com.bookbla.americano.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import lombok.extern.java.Log;

@RestController
@RequestMapping("/member-profiles")
@RequiredArgsConstructor
public class MemberProfileController {

    private final MemberProfileService memberProfileService;

    @PostMapping
    public ResponseEntity<MemberProfileResponse> createMemberProfile(@LoginUser Long memberId,
        @RequestBody @Valid MemberProfileCreateRequest memberProfileCreateRequest) {

        MemberProfileResponse memberProfileResponse = memberProfileService.createMemberProfile(
            memberId, memberProfileCreateRequest.toDto());

        return ResponseEntity.ok(memberProfileResponse);
    }

    @GetMapping
    public ResponseEntity<MemberProfileResponse> readMemberProfile(@LoginUser Long memberId) {
        MemberProfileResponse memberProfileResponse =
            memberProfileService.readMemberProfile(memberId);

        return ResponseEntity.ok(memberProfileResponse);
    }

    @PutMapping
    public ResponseEntity<MemberProfileResponse> updateMemberProfile(
        @RequestBody @Valid MemberProfileUpdateRequest memberProfileUpdateRequest,
        @LoginUser Long memberId) {

        MemberProfileResponse memberProfileResponse =
            memberProfileService.updateMemberProfile(memberId, memberProfileUpdateRequest);

        return ResponseEntity.ok(memberProfileResponse);
    }
}
