package com.bookbla.americano.base.jwt;

import com.bookbla.americano.base.config.JwtConfig;
import com.bookbla.americano.base.exception.AuthExceptionType;
import com.bookbla.americano.base.exception.BaseException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    @Autowired
    private JwtConfig jwtConfig;

    private Key generateKey(String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(String authentication) {
        Date now = new Date();
        Date expireTime = new Date(now.getTime() + jwtConfig.getExpireTime() * 1000);

        return Jwts.builder()
                .setSubject(authentication)
                .signWith(generateKey(jwtConfig.getSecret()), SignatureAlgorithm.HS512)
                .setExpiration(expireTime)
                .compact();
    }

    public void validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(generateKey(jwtConfig.getSecret()))
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

    public Long getPayLoad(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(generateKey(jwtConfig.getSecret()))
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }
}
