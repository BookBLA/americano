package com.bookbla.americano.domain.auth.service;

import com.bookbla.americano.domain.auth.service.dto.OAuth2MemberResponse;
import com.bookbla.americano.domain.member.enums.MemberType;

public interface OAuth2Provider {

    OAuth2MemberResponse getMemberResponse(String authCode);

    MemberType getMemberType();

}
