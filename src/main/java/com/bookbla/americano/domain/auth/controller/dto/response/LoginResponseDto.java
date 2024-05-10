package com.bookbla.americano.domain.auth.controller.dto.response;

import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.bookbla.americano.domain.member.repository.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LoginResponseDto {
    
    private final String accessToken;
    private final Long memberId;
    private final String memberStatus;

    public static LoginResponseDto of(String accessToken, Member member) {
        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .memberId(member.getId())
                .memberStatus(member.getMemberStatus().name())
                .build();
    }
}
