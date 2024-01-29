package com.bookbla.americano.domain.auth.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "jwt")
public class JwtTokenConfig {

    private final String tokenSecretKey;
    private final long tokenValidityInMilliseconds;

}
