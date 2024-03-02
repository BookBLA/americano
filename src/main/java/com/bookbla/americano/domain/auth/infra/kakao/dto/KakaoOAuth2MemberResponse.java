package com.bookbla.americano.domain.auth.infra.kakao.dto;

import com.bookbla.americano.domain.auth.service.dto.OAuth2MemberResponse;
import com.bookbla.americano.domain.member.enums.MemberType;
import com.bookbla.americano.domain.member.repository.entity.Member;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class KakaoOAuth2MemberResponse implements OAuth2MemberResponse {

    private final String email;

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public Member toMember() {
        return Member.builder()
                .oauthEmail(email)
                .memberType(MemberType.KAKAO)
                .build();
    }
}
