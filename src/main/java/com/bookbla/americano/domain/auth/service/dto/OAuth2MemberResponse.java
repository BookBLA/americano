package com.bookbla.americano.domain.auth.service.dto;

import com.bookbla.americano.domain.member.Member;

public interface OAuth2MemberResponse {

    String getEmail();

    Member toMember();
}
