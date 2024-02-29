package com.bookbla.americano.domain.auth.infra.apple;

import com.bookbla.americano.domain.auth.infra.apple.dto.AppleOAuth2MemberResponse;
import com.bookbla.americano.domain.auth.infra.apple.dto.AppleOAuth2Properties;
import com.bookbla.americano.domain.auth.infra.apple.dto.AppleTokenResponse;
import com.bookbla.americano.domain.auth.service.OAuth2Provider;
import com.bookbla.americano.domain.auth.service.dto.OAuth2MemberResponse;
import com.bookbla.americano.domain.member.enums.MemberType;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.PrivateKey;
import java.security.Security;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppleOAuth2Provider implements OAuth2Provider {

    private static final String GRANT_TYPE = "authorization_code";
    private static final String APPLE_TOKEN_DELIMITER = "\\.";
    private static final int TOKEN_INDEX = 1;

    private final AppleOAuth2Client appleOAuth2Client;
    private final AppleOAuth2Properties appleOAuth2Properties;

    @Override
    public OAuth2MemberResponse getMemberResponse(String authCode) {
        AppleTokenResponse response = appleOAuth2Client.getToken(appleOAuth2Properties.getClientId(), generateClientSecret(), GRANT_TYPE, authCode);

        String email = decodeToken(response.getIdToken());

        return new AppleOAuth2MemberResponse(email);
    }

    private String generateClientSecret() {
        return Jwts.builder()
                .setHeaderParam(JwsHeader.KEY_ID, appleOAuth2Properties.getKeyId())
                .setIssuer(appleOAuth2Properties.getTeamId())
                .setSubject(appleOAuth2Properties.getClientId())
                .signWith(generatePrivateKey(), SignatureAlgorithm.ES256)
                .compact();
    }

    private String decodeToken(String token) {
        String encodedToken = token.split(APPLE_TOKEN_DELIMITER)[TOKEN_INDEX];
        String payload = new String(Base64.getUrlDecoder().decode(encodedToken));
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            return objectMapper.readValue(payload, String.class);
        } catch (Exception e) {
            throw new IllegalStateException("애플 토큰 추출 중 예외가 발생했습니다");
        }
    }

    private PrivateKey generatePrivateKey() {
        Security.addProvider(new BouncyCastleProvider());
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");

        try {
            byte[] privateKeyBytes = Base64.getDecoder().decode(appleOAuth2Properties.getPrivateKey());

            return converter.getPrivateKey(PrivateKeyInfo.getInstance(privateKeyBytes));
        } catch (Exception e) {
            throw new IllegalStateException("애플에 전송할 키 생성 중 예외가 발생했습니다~!");
        }
    }

    @Override
    public MemberType getMemberType() {
        return MemberType.APPLE;
    }
}
