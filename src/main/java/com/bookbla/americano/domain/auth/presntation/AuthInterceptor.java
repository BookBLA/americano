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

    // request가 다음 Layer로 전달하기 전에 미리 핸들링, true 반환시 다음으로 넘어감
    // https://www.baeldung.com/spring-mvc-handlerinterceptor
    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        if (CorsUtils.isPreFlightRequest(request) || hasNotAuthorization(request)) {
            return true;
        }

        // Bearer토큰 추출
        String token = BearerTokenExtractor.extract(request);
        // 토큰 검증( 유효하지 않다면 예외 던짐 )
        jwtTokenProvider.validateToken(token);
        return true;
    }

    private boolean hasNotAuthorization(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION) == null;
    }
}
