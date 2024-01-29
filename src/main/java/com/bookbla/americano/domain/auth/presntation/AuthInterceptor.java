package com.bookbla.americano.domain.auth.presntation;


import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.bookbla.americano.domain.auth.domain.JwtTokenProvider;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        if (CorsUtils.isPreFlightRequest(request) || hasNotAuthorization(request)) {
            return true;
        }

        String token = BearerTokenExtractor.extract(request);
        jwtTokenProvider.validateToken(token);
        return true;
    }

    private boolean hasNotAuthorization(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION) == null;
    }
}
