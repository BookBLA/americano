package com.bookbla.americano.domain.postcard.service.dto.response;


import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class SendPostcardResponse {
    private final Boolean isSendSuccess;
}
