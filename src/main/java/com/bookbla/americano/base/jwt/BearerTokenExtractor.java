package com.bookbla.americano.base.jwt;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class BearerTokenExtractor {

    private static final String BEARER_TYPE = "Bearer ";

    // request 중 Bearer 토큰만 추출
    public static String extract(HttpServletRequest request) {
        String authorizationValue = request.getHeader(AUTHORIZATION);
        return authorizationValue.replace(BEARER_TYPE, "").strip();
    }

}
