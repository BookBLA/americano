package com.bookbla.americano.domain.auth.infra.apple.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AppleTokenResponse {

    private String accessToken;
    private String tokenType;
    private Long expiresIn;
    private String refreshToken;
    private String idToken;

}
