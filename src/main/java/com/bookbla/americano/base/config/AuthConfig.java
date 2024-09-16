package com.bookbla.americano.base.config;

import java.util.List;

import com.bookbla.americano.domain.admin.controller.AdminInterceptor;
import com.bookbla.americano.base.jwt.AuthInterceptor;
import com.bookbla.americano.base.resolver.AuthArgumentResolver;
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
                .excludePathPatterns("/error/**")
                .excludePathPatterns("/expoes/**")
                .excludePathPatterns("/alarms/sends/**")
                .excludePathPatterns("/settings/**")
                .excludePathPatterns("/schools")
                .excludePathPatterns("/chat/ws/**")     // 채팅 웹소켓
                .excludePathPatterns("/payment/in-app/apple/notification") // 애플 앱스토어 알림 받는 엔드포인트
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
