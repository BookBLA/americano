package com.bookbla.americano.domain.payment.infrastructure.apple.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "payments.apple")
public class ApplePaymentsProperty {

    private String serverDomain;
    private String keyId;
    private String bundleId;
    private String privateKey;
    private String issuerId;
}
