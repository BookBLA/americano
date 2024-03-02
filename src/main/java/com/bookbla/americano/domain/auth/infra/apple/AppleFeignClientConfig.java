package com.bookbla.americano.domain.auth.infra.apple;

import org.springframework.context.annotation.Bean;

public class AppleFeignClientConfig {

    @Bean
    public AppleFeignErrorDecoder appleOAuth2ClientErrorDecoder() {
        return new AppleFeignErrorDecoder();
    }

}
