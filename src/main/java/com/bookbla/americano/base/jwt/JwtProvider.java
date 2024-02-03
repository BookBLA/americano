package com.bookbla.americano.base.jwt;

import com.bookbla.americano.base.config.JwtConfig;
import com.bookbla.americano.base.exception.AuthExceptionType;
import com.bookbla.americano.base.exception.BaseException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    private final Key secretKey;
    private final long expireTime;

    public JwtProvider(JwtConfig jwtConfig) {
        this.secretKey = generateKey(jwtConfig.getSecret());
        this.expireTime = jwtConfig.getExpireTime() * 1000;
    }

    private Key generateKey(String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(String authentication) {
        Date now = new Date();
        Date expireTime = new Date(now.getTime() + this.expireTime);

        return Jwts.builder()
                .setSubject(authentication)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .setExpiration(expireTime)
                .compact();
    }

    public void validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
        } catch (SecurityException | MalformedJwtException e) {
            throw new BaseException(AuthExceptionType.INVALID_TOKEN_SIGNATURE);
        } catch (ExpiredJwtException e) {
            throw new BaseException(AuthExceptionType.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new BaseException(AuthExceptionType.INVALID_TOKEN);
        }
    }
}
