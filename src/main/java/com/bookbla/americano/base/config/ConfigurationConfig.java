package com.bookbla.americano.base.config;

import com.bookbla.americano.domain.auth.config.JwtTokenConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


// 의미있는 값들을 하나 단위로 묶을 수 있는 Configuration 옵션
// https://www.baeldung.com/spring-boot-configuration-metadata
@Configuration
@EnableConfigurationProperties(JwtTokenConfig.class)
public class ConfigurationConfig {

}
