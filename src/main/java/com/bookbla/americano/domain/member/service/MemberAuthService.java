package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.domain.member.controller.dto.request.MailVerifyRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberAuthCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberAuthUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MailVerifyResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberAuthResponse;
import com.bookbla.americano.domain.member.repository.entity.MemberAuth;
import com.bookbla.americano.domain.member.service.dto.MemberAuthDto;

public interface MemberAuthService {

    MemberAuthResponse createMemberAuth(Long memberId, MemberAuthCreateRequest memberAuthCreateRequest);

    MailVerifyResponse verifyMemberAuth(Long memberId, MailVerifyRequest mailVerifyRequest);

    MemberAuthResponse readMemberAuth(Long memberId);

    MemberAuthResponse updateMemberAuth(Long memberId, MemberAuthUpdateRequest memberAuthUpdateRequest);
}
