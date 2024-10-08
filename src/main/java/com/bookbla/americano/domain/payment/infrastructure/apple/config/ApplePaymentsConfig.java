package com.bookbla.americano.domain.payment.infrastructure.apple.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "payments.apple")
public class ApplePaymentsConfig {

    private String serverDomain;
    private String keyPath;
    private String enviornment;
    private String keyId;
    private String bundleId;
    private String keyFile;
    private long appId;
    private String privateKey;
    private String issuerId;

}
