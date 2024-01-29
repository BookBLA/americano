package com.bookbla.americano.domain.auth.presntation;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import javax.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class BearerTokenExtractor {

    private static final String BEARER_TYPE = "Bearer ";

    public static String extract(HttpServletRequest request) {
        String authorizationValue = request.getHeader(AUTHORIZATION);
        return authorizationValue.replace(BEARER_TYPE, "").strip();
    }

}
