package com.bookbla.americano.domain.member.controller;

import com.bookbla.americano.base.jwt.LoginUser;
import com.bookbla.americano.domain.member.controller.dto.request.MailResendRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MailSendRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MailVerifyRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberAuthStatusUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberAuthUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MailVerifyResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberAuthResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberAuthStatusResponse;
import com.bookbla.americano.domain.member.service.MemberAuthService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

    @PostMapping("/emails")
    public ResponseEntity<MemberAuthResponse> sendMailAndCreateMemberAuth(
        @RequestBody @Valid MailSendRequest mailSendRequest,
        @LoginUser Long memberId) {

        MemberAuthResponse memberAuthResponse =
            memberAuthService.sendEmailAndCreateMemberAuth(memberId, mailSendRequest.toMemberAuthDto());

        return ResponseEntity.ok(memberAuthResponse);
    }

    @PostMapping("/emails/verifications")
    public ResponseEntity<MailVerifyResponse> verifyMail(
        @RequestBody @Valid MailVerifyRequest mailVerifyRequest,
        @LoginUser Long memberId) {

        MailVerifyResponse mailVerifyResponse =
            memberAuthService.verifyEmail(memberId, mailVerifyRequest);

        return ResponseEntity.ok(mailVerifyResponse);
    }

    @PatchMapping("/emails")
    public ResponseEntity<MemberAuthResponse> resendMail(
        @RequestBody @Valid MailResendRequest mailResendRequest,
        @LoginUser Long memberId) {

        MemberAuthResponse memberAuthResponse =
            memberAuthService.resendEmail(memberId, mailResendRequest);

        return ResponseEntity.ok(memberAuthResponse);
    }

    @GetMapping("/statuses")
    public ResponseEntity<MemberAuthStatusResponse> readMemberAuthStatus(
        @LoginUser Long memberId) {

        MemberAuthStatusResponse memberAuthStatusResponse =
            memberAuthService.readMemberAuthStatus(memberId);

        return ResponseEntity.ok(memberAuthStatusResponse);
    }

    @PatchMapping("/statuses")
    public ResponseEntity<MemberAuthStatusResponse> updateMemberAuthStatus(
        @LoginUser Long memberId, MemberAuthStatusUpdateRequest memberAuthStatusUpdateRequest) {

        MemberAuthStatusResponse memberAuthStatusResponse =
            memberAuthService.updateMemberAuthStatus(memberId, memberAuthStatusUpdateRequest);

        return ResponseEntity.ok(memberAuthStatusResponse);
    }

}
