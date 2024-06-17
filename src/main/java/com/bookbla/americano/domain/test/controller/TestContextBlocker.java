package com.bookbla.americano.domain.test.controller;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Aspect
@Configuration
@Profile("prod")
public class TestContextBlocker {

    @Before("execution(* com.bookbla.americano.domain.test.controller.*(..))")
    public void beforeBlockedMethod() {
        throw new UnsupportedOperationException("지원하지 않는 메서드입니다");
    }

}
