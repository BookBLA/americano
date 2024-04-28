package com.bookbla.americano.domain.auth.infra.apple;

import java.security.PrivateKey;
import java.security.Security;
import java.util.Base64;
import java.util.Date;

import com.bookbla.americano.base.jwt.JwtProvider;
import com.bookbla.americano.domain.auth.infra.apple.dto.AppleOAuth2MemberResponse;
import com.bookbla.americano.domain.auth.infra.apple.dto.AppleOAuth2Properties;
import com.bookbla.americano.domain.auth.infra.apple.dto.AppleTokenIdPayload;
import com.bookbla.americano.domain.auth.infra.apple.dto.AppleTokenResponse;
import com.bookbla.americano.domain.auth.service.OAuth2Provider;
import com.bookbla.americano.domain.auth.service.dto.OAuth2MemberResponse;
import com.bookbla.americano.domain.member.enums.MemberType;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.stereotype.Component;

import static java.lang.System.currentTimeMillis;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;

@Component
@RequiredArgsConstructor
public class AppleOAuth2Provider implements OAuth2Provider {

    private static final String GRANT_TYPE = "authorization_code";
    private static final String AUDIENCE = "https://appleid.apple.com";

    private final AppleOAuth2Client appleOAuth2Client;
    private final AppleOAuth2Properties appleOAuth2Properties;
    private final JwtProvider jwtProvider;

    @Override
    public OAuth2MemberResponse getMemberResponse(String authCode) {
        AppleTokenResponse response = appleOAuth2Client.getToken(
                appleOAuth2Properties.getClientId(),
                generateClientSecret(),
                GRANT_TYPE,
                authCode
        );
        String idToken = response.getIdToken();
        AppleTokenIdPayload appleTokenIdPayload = jwtProvider.decodePayload(idToken, AppleTokenIdPayload.class);
        return new AppleOAuth2MemberResponse(appleTokenIdPayload.getEmail());
    }

    // https://developer.apple.com/documentation/accountorganizationaldatasharing/creating-a-client-secret
    private String generateClientSecret() {
        long expiration = MILLISECONDS.convert(5, MINUTES);

        return Jwts.builder()
                .setHeaderParam(JwsHeader.KEY_ID, appleOAuth2Properties.getKeyId())
                .setIssuer(appleOAuth2Properties.getTeamId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(expiration + currentTimeMillis()))
                .setAudience(AUDIENCE)
                .setSubject(appleOAuth2Properties.getClientId())
                .signWith(generatePrivateKey(), SignatureAlgorithm.ES256)
                .compact();
    }

    private PrivateKey generatePrivateKey() {
        Security.addProvider(new BouncyCastleProvider());
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");

        try {
            byte[] privateKeyBytes = Base64.getDecoder().decode(appleOAuth2Properties.getPrivateKey());

            return converter.getPrivateKey(PrivateKeyInfo.getInstance(privateKeyBytes));
        } catch (Exception e) {
            throw new RuntimeException("애플에 전송시 사용할 키 생성 중 예외가 발생했습니다.", e);
        }
    }

    @Override
    public MemberType getMemberType() {
        return MemberType.APPLE;
    }
}
