package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.domain.member.controller.dto.request.EmailResendRequest;
import com.bookbla.americano.domain.member.controller.dto.request.EmailSendRequest;
import com.bookbla.americano.domain.member.controller.dto.request.EmailVerifyRequest;
import com.bookbla.americano.domain.member.controller.dto.response.EmailResponse;

public interface MemberEmailService {

    EmailResponse sendEmail(Long memberId, EmailSendRequest emailSendRequest);

    EmailResponse verifyEmail(Long memberId, EmailVerifyRequest emailVerifyRequest);

    EmailResponse resendEmail(Long memberId, EmailResendRequest emailResendRequest);
}
