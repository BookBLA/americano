package com.bookbla.americano.domain.member.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.member.controller.dto.request.MemberEmailSendRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberEmailVerifyRequest;
import com.bookbla.americano.domain.member.controller.dto.response.EmailResponse;
import com.bookbla.americano.domain.member.service.MemberEmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members/emails")
@RequiredArgsConstructor
public class MemberEmailController {

    private final MemberEmailService memberEmailService;

    @Operation(summary = "이메일 인증 코드 전송 API")
    @PostMapping("/sends")
    public ResponseEntity<EmailResponse> sendEmail(
            @Parameter(hidden = true) @User LoginUser loginUser,
            @RequestBody @Valid MemberEmailSendRequest memberEmailSendRequest) {

        EmailResponse emailResponse = memberEmailService.sendEmail(loginUser.getMemberId(),
            memberEmailSendRequest);

        return ResponseEntity.ok(emailResponse);
    }

    @Operation(summary = "이메일 인증 코드 확인 API")
    @PostMapping("/verifys")
    public ResponseEntity<EmailResponse> verifyEmail(
            @Parameter(hidden = true) @User LoginUser loginUser,
            @RequestBody @Valid MemberEmailVerifyRequest memberEmailVerifyRequest) {

        EmailResponse emailResponse = memberEmailService.verifyEmail(
                loginUser.getMemberId(), memberEmailVerifyRequest);

        return ResponseEntity.ok(emailResponse);
    }

}
