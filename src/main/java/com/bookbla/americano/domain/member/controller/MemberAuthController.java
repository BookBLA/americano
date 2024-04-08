package com.bookbla.americano.domain.member.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.member.controller.dto.request.MailResendRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MailSendRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MailVerifyRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberAuthUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MailVerifyResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberAuthResponse;
import com.bookbla.americano.domain.member.service.MemberAuthService;
import io.swagger.v3.oas.annotations.Parameter;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member-auths")
@RequiredArgsConstructor
public class MemberAuthController {

    private final MemberAuthService memberAuthService;

    @GetMapping
    public ResponseEntity<MemberAuthResponse> readMemberAuth(
            @Parameter(hidden = true) @User LoginUser loginUser) {
        MemberAuthResponse memberAuthResponse = memberAuthService.readMemberAuth(
                loginUser.getMemberId());
        return ResponseEntity.ok(memberAuthResponse);
    }

    @PutMapping
    public ResponseEntity<MemberAuthResponse> updateMemberAuth(
            @Parameter(hidden = true) @User LoginUser loginUser,
            @RequestBody @Valid MemberAuthUpdateRequest memberAuthUpdateRequest) {

        MemberAuthResponse memberAuthUpdateResponse =
                memberAuthService.updateMemberAuth(loginUser.getMemberId(), memberAuthUpdateRequest);

        return ResponseEntity.ok(memberAuthUpdateResponse);
    }

    @PostMapping("/emails")
    public ResponseEntity<MemberAuthResponse> sendMailAndCreateMemberAuth(
            @Parameter(hidden = true) @User LoginUser loginUser,
            @RequestBody @Valid MailSendRequest mailSendRequest) {

        MemberAuthResponse memberAuthResponse =
                memberAuthService.sendEmailAndCreateMemberAuth(loginUser.getMemberId(),
                        mailSendRequest.toMemberAuthDto());

        return ResponseEntity.ok(memberAuthResponse);
    }

    @PostMapping("/emails/verifications")
    public ResponseEntity<MailVerifyResponse> verifyMail(
            @Parameter(hidden = true) @User LoginUser loginUser,
            @RequestBody @Valid MailVerifyRequest mailVerifyRequest) {

        MailVerifyResponse mailVerifyResponse =
                memberAuthService.verifyEmail(loginUser.getMemberId(), mailVerifyRequest);

        return ResponseEntity.ok(mailVerifyResponse);
    }

    @PatchMapping("/emails")
    public ResponseEntity<MemberAuthResponse> resendMail(
            @Parameter(hidden = true) @User LoginUser loginUser,
            @RequestBody @Valid MailResendRequest mailResendRequest) {

        MemberAuthResponse memberAuthResponse =
                memberAuthService.resendEmail(loginUser.getMemberId(), mailResendRequest);

        return ResponseEntity.ok(memberAuthResponse);
    }

}
