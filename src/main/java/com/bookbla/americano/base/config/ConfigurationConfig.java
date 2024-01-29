package com.bookbla.americano.base.config;

import com.bookbla.americano.domain.auth.config.JwtTokenConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JwtTokenConfig.class)
public class ConfigurationConfig {

}
