package com.bookbla.americano.base.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 기존에 등록된 StringHttpMessageConverter를 제거
        converters.removeIf(converter -> converter instanceof StringHttpMessageConverter);

        // JSON 형식으로 응답을 처리하는 MappingJackson2HttpMessageConverter 등록
        converters.add(new MappingJackson2HttpMessageConverter());
    }
}