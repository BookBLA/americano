package com.bookbla.americano.domain.member.controller;

import com.bookbla.americano.base.jwt.LoginUser;
import com.bookbla.americano.domain.member.controller.dto.request.MailVerifyRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberAuthCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberAuthUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MailVerifyResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberAuthResponse;
import com.bookbla.americano.domain.member.service.MemberAuthService;
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
@RequestMapping("/member-auths")
@RequiredArgsConstructor
public class MemberAuthController {

    private final MemberAuthService memberAuthService;

    @PostMapping
    public ResponseEntity<MemberAuthResponse> createMemberAuth(
        @RequestBody @Valid MemberAuthCreateRequest memberAuthCreateRequest,
        @LoginUser Long memberId) {

        MemberAuthResponse memberAuthResponse =
            memberAuthService.createMemberAuth(memberId, memberAuthCreateRequest);

        return ResponseEntity.created(URI.create("/member-auths" +
            memberAuthResponse.getMemberAuthId()))
            .body(memberAuthResponse);
    }

    @GetMapping
    public ResponseEntity<MemberAuthResponse> readMemberAuth(@LoginUser Long memberId) {
        MemberAuthResponse memberAuthResponse = memberAuthService.readMemberAuth(memberId);
        return ResponseEntity.ok(memberAuthResponse);
    }

    @PutMapping
    public ResponseEntity<MemberAuthResponse> updateMemberAuth(
        @RequestBody @Valid MemberAuthUpdateRequest memberAuthUpdateRequest,
        @LoginUser Long memberId) {

        MemberAuthResponse memberAuthUpdateResponse =
            memberAuthService.updateMemberAuth(memberId, memberAuthUpdateRequest);

        return ResponseEntity.ok(memberAuthUpdateResponse);
    }

    @PostMapping("/emails/verifications")
    public ResponseEntity<MailVerifyResponse> verifyMemberAuth(
        @RequestBody @Valid MailVerifyRequest mailVerifyRequest,
        @LoginUser Long memberId) {

        MailVerifyResponse mailVerifyResponse =
            memberAuthService.verifyMemberAuth(memberId, mailVerifyRequest);

        return ResponseEntity.ok(mailVerifyResponse);
    }

}
