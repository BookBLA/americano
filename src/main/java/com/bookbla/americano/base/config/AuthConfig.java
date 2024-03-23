package com.bookbla.americano.base.config;

import com.bookbla.americano.base.interceptor.AuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class AuthConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    // 인터셉터 사용 위해 필요
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
            .excludePathPatterns("/tests/**")
            .excludePathPatterns("/auth/**")
            .excludePathPatterns("/swagger-ui/**")
            .excludePathPatterns("/api-docs/**")
            .excludePathPatterns("/styles/**");
    }
}
