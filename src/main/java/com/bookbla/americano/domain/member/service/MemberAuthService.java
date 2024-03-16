package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.domain.member.controller.dto.request.MailResendRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MailVerifyRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberAuthUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MailSendResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MailVerifyResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberAuthResponse;
import com.bookbla.americano.domain.member.service.dto.MemberAuthDto;

public interface MemberAuthService {

    MailSendResponse sendEmailAndCreateMemberAuth(Long memberId, MemberAuthDto memberAuthDto);

    MailVerifyResponse verifyEmail(Long memberId, MailVerifyRequest mailVerifyRequest);

    MailSendResponse resendEmail(Long memberId, MailResendRequest mailResendRequest);

    MemberAuthResponse readMemberAuth(Long memberId);

    MemberAuthResponse updateMemberAuth(Long memberId, MemberAuthUpdateRequest memberAuthUpdateRequest);

}
