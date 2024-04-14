package com.bookbla.americano.domain.auth.infra.apple.dto;

import com.bookbla.americano.domain.auth.repository.entity.MemberSignUpInformation;
import com.bookbla.americano.domain.auth.service.dto.OAuth2MemberResponse;
import com.bookbla.americano.domain.member.enums.MemberType;
import com.bookbla.americano.domain.member.repository.entity.Member;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AppleOAuth2MemberResponse implements OAuth2MemberResponse {

    private final String email;

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public Member toMember() {
        return Member.builder()
                .oauthEmail(email)
                .memberType(MemberType.APPLE)
                .build();
    }
}
