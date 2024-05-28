package com.bookbla.americano.domain.alarm.service;

import com.bookbla.americano.domain.alarm.controller.dto.request.MemberTokenCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberTokenCreateResponse;

public interface MemberTokenService {

    MemberTokenCreateResponse createPushToken(Long memberId, MemberTokenCreateRequest memberTokenCreateRequest);
}
