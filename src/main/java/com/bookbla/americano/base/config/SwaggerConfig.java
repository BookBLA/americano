package com.bookbla.americano.base.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        servers = {
                @Server(url = "http://localhost:8080")
        },

        info = @Info(
                title = "BookBLA API Document",
                description = "북블라 API 문서",
                version = "v1"))

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi nonSecurityGroupOpenApi() {
        return GroupedOpenApi
                .builder()
                .group("Open API Group")
                .pathsToMatch("/**")
                .build();
    }
}