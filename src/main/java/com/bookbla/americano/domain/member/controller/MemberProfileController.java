package com.bookbla.americano.domain.member.controller;

import com.bookbla.americano.base.jwt.LoginUser;
import com.bookbla.americano.domain.member.controller.dto.request.MemberProfileCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberProfileUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberProfileResponse;
import com.bookbla.americano.domain.member.service.MemberProfileService;
import java.net.URI;
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
@RequestMapping("/member-profiles")
@RequiredArgsConstructor
public class MemberProfileController {

    private final MemberProfileService memberProfileService;

    @PostMapping
    public ResponseEntity<MemberProfileResponse> createMemberProfile(
        @RequestBody @Valid MemberProfileCreateRequest memberProfileCreateRequest,
        @LoginUser Long memberId) {

        MemberProfileResponse memberProfileResponse =
            memberProfileService.createMemberProfile(memberId, memberProfileCreateRequest);

        return ResponseEntity.created(URI.create("/member-profiles/" +
            memberProfileResponse.getMemberProfileId()))
            .body(memberProfileResponse);
    }

    @GetMapping
    public ResponseEntity<MemberProfileResponse> createMemberProfile(@LoginUser Long memberId) {
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
