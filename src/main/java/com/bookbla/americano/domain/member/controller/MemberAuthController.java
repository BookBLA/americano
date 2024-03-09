package com.bookbla.americano.domain.member.controller;

import com.bookbla.americano.base.jwt.LoginUser;
import com.bookbla.americano.domain.member.controller.dto.request.MailResendRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MailSendRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MailVerifyRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberAuthUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MailSendResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MailVerifyResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberAuthResponse;
import com.bookbla.americano.domain.member.service.MemberAuthService;
import com.bookbla.americano.domain.member.service.MemberPolicyService;
import com.bookbla.americano.domain.member.service.MemberProfileService;
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
    private final MemberPolicyService memberPolicyService;
    private final MemberProfileService memberProfileService;

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
    public ResponseEntity<MailSendResponse> sendMailAndCreateMemberAuth(
        @RequestBody @Valid MailSendRequest mailSendRequest,
        @LoginUser Long memberId) {

        MailSendResponse mailSendResponse =
            memberAuthService.createMemberAuth(memberId, mailSendRequest.toMemberAuthDto());

        return ResponseEntity.ok(mailSendResponse);
    }

    @PostMapping("/emails/verifications")
    public ResponseEntity<MailVerifyResponse> verifyMemberAuth(
        @RequestBody @Valid MailVerifyRequest mailVerifyRequest,
        @LoginUser Long memberId) {

        MailVerifyResponse mailVerifyResponse =
            memberAuthService.verifyMemberAuth(memberId, mailVerifyRequest);

        return ResponseEntity.ok(mailVerifyResponse);
    }

    @PatchMapping("/emails")
    public ResponseEntity<MailSendResponse> resendMail(
        @RequestBody @Valid MailResendRequest mailResendRequest,
        @LoginUser Long memberId) {

        MailSendResponse mailSendResponse =
            memberAuthService.resendMail(memberId, mailResendRequest);

        return ResponseEntity.ok(mailSendResponse);
    }

}
