package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.domain.member.controller.dto.request.MemberAuthCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberAuthUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberAuthResponse;
import com.bookbla.americano.domain.member.repository.entity.MemberAuth;
import com.bookbla.americano.domain.member.service.dto.MemberAuthDto;

public interface MemberAuthService {

    MemberAuthResponse createMemberAuth(Long memberId, MemberAuthCreateRequest memberAuthCreateRequest);

    void verifyMemberAuth(Long memberId, String inputVerifyCode);

    MemberAuthResponse readMemberAuth(Long memberId);

    MemberAuthResponse updateMemberAuth(Long memberId, MemberAuthUpdateRequest memberAuthUpdateRequest);
}
