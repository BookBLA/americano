package com.bookbla.americano.domain.member.controller;

import com.bookbla.americano.base.jwt.LoginUser;
import com.bookbla.americano.domain.member.controller.dto.request.MailSendRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MailVerifyRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MailSendResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MailVerifyResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberResponse;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.service.MailService;
import com.bookbla.americano.domain.member.service.MemberService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MailService mailService;

    @PostMapping("/emails/verification-requests")
    public ResponseEntity<MailSendResponse> sendEmail(
        @RequestBody @Valid MailSendRequest mailSendRequest,
        @LoginUser Long memberId) {

        mailService.sendEmail(memberId, mailSendRequest.getSchoolEmail());

        return ResponseEntity.ok()
            .body(MailSendResponse.builder()
                .message("메일 전송 성공")
                .build());
    }

    @PostMapping("/emails/verifications")
    public ResponseEntity<MailVerifyResponse> verifyEmail(
        @RequestBody @Valid MailVerifyRequest mailVerifyRequest,
        @LoginUser Long memberId) {

        mailService.verifyEmail(memberId, mailVerifyRequest.getVerifyCode());

        return ResponseEntity.ok()
            .body(MailVerifyResponse.builder()
                .message("메일 인증 성공")
                .build());
    }

}
