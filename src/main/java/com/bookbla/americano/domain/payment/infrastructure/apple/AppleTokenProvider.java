package com.bookbla.americano.domain.payment.infrastructure.apple;

import java.security.PrivateKey;
import java.security.Security;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import com.bookbla.americano.domain.payment.infrastructure.apple.config.ApplePaymentsConfig;
import com.bookbla.americano.domain.payment.infrastructure.apple.tokens.DecodedTokenHeader;
import com.bookbla.americano.domain.payment.infrastructure.apple.tokens.DecodedTokenPayload;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.MediaType;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMException;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.stereotype.Component;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static io.jsonwebtoken.JwsHeader.ALGORITHM;
import static io.jsonwebtoken.JwsHeader.KEY_ID;
import static io.jsonwebtoken.SignatureAlgorithm.ES256;
import static java.lang.System.currentTimeMillis;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;

@RequiredArgsConstructor
@Component
class AppleTokenProvider {

    private static final String APPSTORE_CONNECT_AUDIENCE = "appstoreconnect-v1";
    private static final String TOKEN_TYPE_KEY = "typ";
    private static final String BUNDLE_ID_KEY = "bid";

    private final ApplePaymentsConfig config;

    // https://developer.apple.com/documentation/appstoreserverapi/generating_json_web_tokens_for_api_requests
    public String createRequestToken() {
        long expirations = MILLISECONDS.convert(10, MINUTES);

        return Jwts.builder()
                .setHeaderParams(toHeaders())
                .setIssuer(config.getIssuerId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(expirations + currentTimeMillis()))
                .setAudience(APPSTORE_CONNECT_AUDIENCE)
                .setClaims(Map.of(BUNDLE_ID_KEY, config.getBundleId()))
                .signWith(generatePrivateKey(), ES256)
                .compact();
    }

    private Map<String, Object> toHeaders() {
        return Map.of(
                KEY_ID, config.getKeyId(),
                ALGORITHM, ES256.getValue(),
                TOKEN_TYPE_KEY, MediaType.JWT.toString()
        );
    }

    private PrivateKey generatePrivateKey() {
        Security.addProvider(new BouncyCastleProvider());
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
        try {
            byte[] privateKeyBytes = Base64.getDecoder().decode(config.getPrivateKey());
            return converter.getPrivateKey(PrivateKeyInfo.getInstance(privateKeyBytes));
        } catch (PEMException e) {
            throw new RuntimeException("애플 전송시 사용할 토큰의 개인키 생성 중 예외가 발생했습니다.");
        }
    }

    public DecodedTokenHeader decodeHeader(String token) {
        String encodedHeader = token.split("\\.")[0];
        Base64.Decoder base64Decoder = Base64.getUrlDecoder();
        String payload = new String(base64Decoder.decode(encodedHeader));
        ObjectMapper objectMapper = new ObjectMapper().configure(FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            return objectMapper.readValue(payload, DecodedTokenHeader.class);
        } catch (Exception e) {
            throw new RuntimeException("토큰 디코딩 중 예외가 발생했습니다.", e);
        }
    }

    public DecodedTokenPayload decodePayload(String token) {
        String encodedPayload = token.split("\\.")[1];
        Base64.Decoder base64Decoder = Base64.getUrlDecoder();
        String payload = new String(base64Decoder.decode(encodedPayload));
        ObjectMapper objectMapper = new ObjectMapper().configure(FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            return objectMapper.readValue(payload, DecodedTokenPayload.class);
        } catch (Exception e) {
            throw new RuntimeException("토큰 디코딩 중 예외가 발생했습니다.", e);
        }
    }
}
