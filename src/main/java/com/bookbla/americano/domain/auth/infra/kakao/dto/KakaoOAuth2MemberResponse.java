package com.bookbla.americano.domain.auth.infra.kakao.dto;

import com.bookbla.americano.domain.auth.service.dto.OAuth2MemberResponse;
import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.bookbla.americano.domain.member.enums.MemberType;
import com.bookbla.americano.domain.member.repository.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class KakaoOAuth2MemberResponse implements OAuth2MemberResponse {

    private final String email;
    private final String profileImageUrl;

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public Member toMember() {
        return Member.builder()
                .oauthEmail(email)
                .memberType(MemberType.KAKAO)
                .memberStatus(MemberStatus.PROFILE)
                .build();
    }
}
