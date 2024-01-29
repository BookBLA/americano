package com.bookbla.americano.domain.auth.domain;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.base.exception.BaseExceptionType;
import com.bookbla.americano.domain.auth.config.JwtTokenConfig;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private final Key secretKey;
    private final long expireTime;

    public JwtTokenProvider(JwtTokenConfig jwtTokenConfig) {
        this.secretKey = generateKey(jwtTokenConfig.getSecret());
        this.expireTime = jwtTokenConfig.getExpireTime() * 1000;
    }

    private Key generateKey(String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(String authentication) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + expireTime);

        return Jwts.builder()
                .setSubject(authentication)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    public void validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
        } catch (SecurityException | MalformedJwtException e) {
            throw new BaseException(BaseExceptionType.TEST_FAIL);
        } catch (ExpiredJwtException e) {
            throw new BaseException(BaseExceptionType.TEST_FAIL);
        } catch (UnsupportedJwtException e) {
            throw new BaseException(BaseExceptionType.TEST_FAIL);
        }
    }
}
