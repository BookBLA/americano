package com.bookbla.americano.domain.payment.infrastructure.apple;

import java.security.PrivateKey;
import java.security.Security;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import com.bookbla.americano.domain.payment.infrastructure.apple.config.ApplePaymentsProperty;
import com.google.common.net.MediaType;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMException;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.stereotype.Component;

import static io.jsonwebtoken.JwsHeader.ALGORITHM;
import static io.jsonwebtoken.JwsHeader.KEY_ID;
import static io.jsonwebtoken.SignatureAlgorithm.ES256;
import static java.lang.System.currentTimeMillis;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;

@RequiredArgsConstructor
@Component
final class AppleJwtProvider {

    private static final String APPSTORE_CONNECT_AUDIENCE = "appstoreconnect-v1";
    private static final String TOKEN_TYPE_KEY = "typ";
    private static final String BUNDLE_ID_KEY = "bid";

    private final ApplePaymentsProperty property;

    // https://developer.apple.com/documentation/appstoreserverapi/generating_json_web_tokens_for_api_requests
    public String createToken() {
        long expirations = MILLISECONDS.convert(10, MINUTES);

        return Jwts.builder()
                .setHeaderParams(toHeaders())
                .setIssuer(property.getIssuerId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(expirations + currentTimeMillis()))
                .setAudience(APPSTORE_CONNECT_AUDIENCE)
                .setClaims(Map.of(BUNDLE_ID_KEY, property.getBundleId()))
                .signWith(generatePrivateKey(), ES256)
                .compact();
    }

    private Map<String, Object> toHeaders() {
        return Map.of(
                KEY_ID, property.getKeyId(),
                ALGORITHM, ES256.getValue(),
                TOKEN_TYPE_KEY, MediaType.JWT.toString()
        );
    }

    private PrivateKey generatePrivateKey() {
        Security.addProvider(new BouncyCastleProvider());
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
        try {
            byte[] privateKeyBytes = Base64.getDecoder().decode(property.getPrivateKey());
            return converter.getPrivateKey(PrivateKeyInfo.getInstance(privateKeyBytes));
        } catch (PEMException e) {
            throw new RuntimeException("애플 전송시 사용할 토큰의 개인키 생성 중 예외가 발생했습니다.");
        }
    }
}
