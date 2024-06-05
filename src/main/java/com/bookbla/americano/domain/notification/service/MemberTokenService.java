package com.bookbla.americano.domain.notification.service;

import com.bookbla.americano.domain.notification.controller.dto.request.MemberTokenCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberTokenCreateResponse;

public interface MemberTokenService {

    MemberTokenCreateResponse createPushToken(Long memberId, MemberTokenCreateRequest memberTokenCreateRequest);
}
