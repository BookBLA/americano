package com.bookbla.americano.domain.payment.infrastructure.apple;

import java.util.Base64;

import com.bookbla.americano.domain.payment.infrastructure.apple.tokens.DecodedTokenPayload;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

@RequiredArgsConstructor
@Component
@Slf4j
class AppleTokenProvider {

    private static final String JWT_ELEMENT_DELIMITER = "\\.";
    private static final int TOKEN_PAYLOAD_INDEX = 1;

    public DecodedTokenPayload decodePayload(String token) {
        String encodedPayload = token.split(JWT_ELEMENT_DELIMITER)[TOKEN_PAYLOAD_INDEX];
        Base64.Decoder base64Decoder = Base64.getUrlDecoder();
        String payload = new String(base64Decoder.decode(encodedPayload));
        ObjectMapper objectMapper = new ObjectMapper().configure(FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            return objectMapper.readValue(payload, DecodedTokenPayload.class);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("토큰 디코딩 중 예외가 발생했습니다.", e);
        }
    }
}
