package com.bookbla.americano.domain.auth.domain;

import com.bookbla.americano.base.exception.AuthErrorType;
import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.base.exception.BaseExceptionType;
import com.bookbla.americano.base.exception.ExceptionType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenProvider implements InitializingBean {

    private final String tokenSecretKey;
    private final long tokenValidityInMilliseconds;
    private Key tokenKey;

    public TokenProvider(
            @Value("${jwt.token-secret-key}") String tokenSecretKey,
            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds) {
        this.tokenSecretKey = tokenSecretKey;
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
    }

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(tokenSecretKey);
        this.tokenKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(tokenKey).build().parseClaimsJws(token.substring(0, 7));
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            throw new BaseException(BaseExceptionType.TEST_FAIL);
        } catch (ExpiredJwtException e) {
            throw new BaseException(BaseExceptionType.TEST_FAIL);
        } catch (UnsupportedJwtException e) {
            throw new BaseException(BaseExceptionType.TEST_FAIL);
        }
    }
}
