package com.bookbla.americano.domain.auth.infra.apple.dto;

import com.bookbla.americano.domain.auth.repository.entity.MemberSignUpInformation;
import com.bookbla.americano.domain.auth.service.dto.OAuth2MemberResponse;
import com.bookbla.americano.domain.member.enums.MemberType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AppleOAuth2MemberResponse implements OAuth2MemberResponse {

    private final String email;

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public MemberSignUpInformation toMemberSignUpInformation() {
        return MemberSignUpInformation.builder()
                .email(email)
                .memberType(MemberType.APPLE)
                .build();
    }
}
