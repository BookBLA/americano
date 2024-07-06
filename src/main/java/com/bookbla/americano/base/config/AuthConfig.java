package com.bookbla.americano.base.config;

import com.bookbla.americano.base.interceptor.AdminInterceptor;
import com.bookbla.americano.base.interceptor.AuthInterceptor;
import com.bookbla.americano.base.resolver.AuthArgumentResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class AuthConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;
    private final AdminInterceptor adminInterceptor;
    private final AuthArgumentResolver authArgumentResolver;

    // 인터셉터 사용 위해 필요
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 사용자 인터셉터
        registry.addInterceptor(authInterceptor)
                .excludePathPatterns("/tests/**")
                .excludePathPatterns("/auth/**")
                .excludePathPatterns("/swagger-ui/**")
                .excludePathPatterns("/api-docs/**")
                .excludePathPatterns("/styles/**")
                .excludePathPatterns("/admin/**")
                .excludePathPatterns("/aws/s3/**")
                .excludePathPatterns("/error/**")
                .excludePathPatterns("/expoes/**")
            .excludePathPatterns("/alarms/sends/**")
            .excludePathPatterns("/settings/**")
            .excludePathPatterns("/schools/**")
        ;

        // 어드민 인터셉터
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/auth/login");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(authArgumentResolver);
    }
}
