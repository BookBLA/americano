package com.bookbla.americano.base.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class BaseErrorResponseDTO {

    private final boolean isSuccess;
    private final String longMessage;
    private final String shortMessage;
    private final String errorCode;

    public static BaseErrorResponseDTO systemError(String longMessage, String shortMessage) {
        return BaseErrorResponseDTO.builder()
                .isSuccess(false)
                .longMessage(longMessage)
                .shortMessage(shortMessage)
                .build();
    }

    public static BaseErrorResponseDTO error(
            String errorCode,
            String longMessage,
            String shortMessage) {
        return BaseErrorResponseDTO.builder()
                .isSuccess(false)
                .errorCode(errorCode)
                .longMessage(longMessage)
                .shortMessage(shortMessage)
                .build();
    }
}