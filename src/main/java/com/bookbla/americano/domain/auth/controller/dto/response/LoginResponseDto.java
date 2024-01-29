package com.bookbla.americano.domain.auth.controller.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class LoginResponseDto {
    private final String token;
    private final Long tokenExpireTime;
}
