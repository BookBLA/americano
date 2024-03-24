package com.bookbla.americano.domain.auth.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LoginResponseDto {
    
    private final String accessToken;

    public static LoginResponseDto from(String accessToken) {
        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .build();
    }
}
