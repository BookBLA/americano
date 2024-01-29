package com.bookbla.americano.base.config;

import com.bookbla.americano.base.util.SecurityUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

@Configuration
public class JpaAuditConfig {
    @Bean
    public AuditorAware<String> auditorProvider() {
        return SecurityUtil::getCurrentUserEmail;
    }
}
