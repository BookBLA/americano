package com.bookbla.americano.domain.test.controller;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.base.exception.BaseExceptionType;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Aspect
@Configuration
@Profile("!dev & !local")
public class TestContextBlocker {

    @Before("execution(* com.bookbla.americano.domain.test.controller.TestController.*(..))")
    public void beforeBlockedMethod() {
        throw new BaseException(BaseExceptionType.NOT_VALID_METHODS);
    }

}
