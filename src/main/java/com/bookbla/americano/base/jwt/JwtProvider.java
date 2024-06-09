package com.bookbla.americano.base.jwt;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import com.bookbla.americano.base.config.JwtConfig;
import com.bookbla.americano.base.exception.AuthExceptionType;
import com.bookbla.americano.base.exception.BaseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
            parseToken(token);
        } catch (SecurityException | MalformedJwtException e) {
            throw new BaseException(AuthExceptionType.INVALID_TOKEN_SIGNATURE);
        } catch (ExpiredJwtException e) {
            throw new BaseException(AuthExceptionType.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new BaseException(AuthExceptionType.INVALID_TOKEN);
        }
    }

    public String decodeToken(String token) {
        return parseToken(token).getBody().getSubject();
    }

    private Jws<Claims> parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(generateKey(jwtConfig.getSecret()))
                .build()
                .parseClaimsJws(token);
    }

    public static <T> T decodePayload(String token, Class<T> clazz) {
        String jwtPayload = token.split("\\.")[1];
        Base64.Decoder base64Decoder = Base64.getUrlDecoder();
        String payload = new String(base64Decoder.decode(jwtPayload));
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            return objectMapper.readValue(payload, clazz);
        } catch (Exception e) {
            throw new IllegalStateException("토큰의 페이로드 추출 중 예외가 발생했습니다.", e);
        }
    }
}
