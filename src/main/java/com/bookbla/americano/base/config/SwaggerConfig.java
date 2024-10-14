package com.bookbla.americano.base.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@SecurityScheme(
    name = "Bearer Authentication",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)

@OpenAPIDefinition(
    servers = {
        @Server(url = "http://localhost:8080/api"),
        @Server(url = "https://dev.bookbla.shop/api"),
        @Server(url = "https://prod.bookbla.shop/api")
    },

    info = @Info(
        title = "BookBLA API Document",
        description = "북블라 API 문서",
        version = "v1"),

    security = @SecurityRequirement(name = "Bearer Authentication")
)
@Profile({"local", "dev"})
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
