package com.bookbla.americano.base.log.discord;

import com.bookbla.americano.base.log.LogLevel;
import com.bookbla.americano.base.log.RequestStorage;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@AutoConfigurationPackage
@Profile({"prod", "dev"})
@RequiredArgsConstructor
@Component
@Aspect
public class ExceptionAppender {

    private final RequestStorage requestStorage;
    private final DiscordMessage discordMessage;
    private final BookblaLogDiscord bookblaLogDiscord;

    @Before("@annotation(com.bookbla.americano.base.log.discord.DiscordAlarm)")
    public void appendExceptionToResponseBody(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Object arg = args[1]; // args[0] : ContentCachingRequestWrapper

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        DiscordAlarm annotation = signature.getMethod().getAnnotation(DiscordAlarm.class);
        LogLevel logLevel = annotation.level();

        String message = discordMessage.generate(
                requestStorage.getRequest(),
                logLevel,
                (Exception) arg
        );
        bookblaLogDiscord.sendMessage(message);
    }
}
