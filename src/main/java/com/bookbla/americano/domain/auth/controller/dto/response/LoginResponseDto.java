package com.bookbla.americano.domain.auth.controller.dto.response;

import com.bookbla.americano.domain.auth.repository.entity.MemberSignUpInformation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LoginResponseDto {
    
    private final String accessToken;
    private final Long memberSignUpInformationId;

    public static LoginResponseDto of(String accessToken, MemberSignUpInformation member) {
        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .memberSignUpInformationId(member.getId())
                .build();
    }
}
