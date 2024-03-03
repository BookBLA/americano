package com.bookbla.americano.domain.auth.infra.apple;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AppleFeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String method, Response response) {
        if (response != null && response.body() != null) {
            try {
                new ObjectMapper().readValue(response.body().toString(), Object.class);
            } catch (Exception e) {
                log.error("응답 decoding 중 예외 발생", e.getMessage());
            }
        }

        return new IllegalStateException("애플 소셜 로그인 호출 오류");
    }

}
