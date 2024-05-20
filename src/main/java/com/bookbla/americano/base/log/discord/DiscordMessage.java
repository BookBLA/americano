package com.bookbla.americano.base.log.discord;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.bookbla.americano.base.jwt.BearerTokenExtractor;
import com.bookbla.americano.base.jwt.JwtProvider;
import com.bookbla.americano.base.log.LogLevel;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

import static org.apache.http.HttpHeaders.AUTHORIZATION;

@RequiredArgsConstructor
@Component
public class DiscordMessage {

    private static final int MAX_MESSAGE_SIZE = 2_000;
    private static final String CRLF = "\n";
    private static final String EXTRACTION_ERROR_MESSAGE = "메시지 추출중 예외가 발생했습니다.\nmessage : %s";
    private static final String EXCEPTION_MESSAGE_FORMAT = "_%s_ %s.%s:%d - %s";
    private static final String MESSAGE_FORMAT = "\n\n**[요청한 멤버 id]**\n%s\n\n" +
            "**[요청 시간]**\n%s\n\n" +
            "**[에러 로그]**\n%s\n[%s] %s\n\n" +
            "*[Header]*\n%s\n\n" +
            "*[Body]*\n%s\n\n";

    private final JwtProvider jwtProvider;

    public String generate(
            ContentCachingRequestWrapper request,
            LogLevel logLevel,
            Exception exception
    ) {
        try {
            String memberId = findMemberId(request);
            String currentTime = LocalDateTime.now().toString();
            String method = request.getMethod();
            String requestURI = request.getRequestURI();
            String headers = extractHeaders(request);
            String body = getBody(request);
            String message = extractExceptionMessage(exception, logLevel);

            return toMessage(currentTime, memberId, message, method, requestURI, headers, body);
        } catch (Exception e) {
            return String.format(EXTRACTION_ERROR_MESSAGE, e);
        }
    }

    private String findMemberId(HttpServletRequest request) {
        if (request.getHeader(AUTHORIZATION) == null) {
            return "Guest";
        }
        String token = BearerTokenExtractor.extract(request);
        return jwtProvider.decodeToken(token);
    }

    private String extractHeaders(ContentCachingRequestWrapper request) {
        Enumeration<String> headers = request.getHeaderNames();
        Map<String, String> headerMap = new HashMap<>();

        while (headers.hasMoreElements()) {
            String headerName = headers.nextElement();
            headerMap.put(headerName, request.getHeader(headerName));
        }

        return headerMap.keySet().stream()
                .map(key -> key + ": " + headerMap.get(key))
                .collect(Collectors.joining(CRLF));
    }

    private String getBody(ContentCachingRequestWrapper request) {
        String body = new String(request.getContentAsByteArray());
        if (body.isEmpty()) {
            body = "Body is Empty";
        }
        return body;
    }

    private String extractExceptionMessage(Exception e, LogLevel logLevel) {
        StackTraceElement stackTrace = e.getStackTrace()[0];
        String className = stackTrace.getClassName();
        int lineNumber = stackTrace.getLineNumber();
        String method = stackTrace.getMethodName();

        String message = e.getMessage();

        if (Objects.isNull(message)) {
            return Arrays.stream(e.getStackTrace())
                    .map(StackTraceElement::toString)
                    .collect(Collectors.joining(CRLF));
        }

        return String.format(
                EXCEPTION_MESSAGE_FORMAT, logLevel.name(), className,
                method, lineNumber, message
        );
    }

    private String toMessage(
            String currentTime, String memberId, String errorMessage,
            String method, String requestURI, String headers, String body
    ) {
        String message = String.format(
                MESSAGE_FORMAT, memberId, currentTime,
                errorMessage, method, requestURI, headers, body
        );
        return message.length() < MAX_MESSAGE_SIZE
                ? message : message.substring(0, MAX_MESSAGE_SIZE - 1);
    }
}
