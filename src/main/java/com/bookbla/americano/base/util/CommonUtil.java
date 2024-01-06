package com.bookbla.americano.base.util;

import com.bookbla.americano.base.dto.BaseResponseDTO;
import com.bookbla.americano.base.enums.exception.CommonErrorCodeEnum;
import com.bookbla.americano.base.exceptions.BizException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
public class CommonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public static String ObjectToJsonString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new BizException(CommonErrorCodeEnum.JSON_MARSHALLING_ERROR);
        }
    }

    public static BaseResponseDTO objectToDefaultResponseDTO(Object object) {
        try {
            return BaseResponseDTO.create(object);
        } catch (Exception e) {
            log.debug(e.getMessage());
            throw new BizException(CommonErrorCodeEnum.JSON_MARSHALLING_ERROR);
        }
    }

    public static URI convertStringToUri(String string) {
        log.debug(objectMapper.toString());
        try {
            return new URI(string);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}