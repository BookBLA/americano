package com.bookbla.americano.base.handler;

import com.bookbla.americano.base.dto.BaseErrorResponseDTO;
import com.bookbla.americano.base.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice({
        "com.bookbla.americano.interfaces.controller",
})
public class BaseResponseHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response) {

        if (selectedContentType.isCompatibleWith(MediaType.APPLICATION_JSON)) {
            if (body instanceof BaseErrorResponseDTO) {
                return body;
            } else {
                return CommonUtil.objectToDefaultResponseDTO(body);
            }
        } else if (selectedContentType.isCompatibleWith(MediaType.TEXT_PLAIN)) {
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return CommonUtil.objectToDefaultResponseDTO(body);
        } else {
            return body;
        }
    }
}