package com.bookbla.americano.domain.auth.infra.apple.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "oauth.apple")
public class AppleOAuth2Properties {

    private String clientId;
    private String keyId;
    private String teamId;
    private String privateKey;

}
