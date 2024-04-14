package com.bookbla.americano.domain.postcard.service;


import com.bookbla.americano.domain.postcard.service.dto.request.SendPostcardRequest;
import com.bookbla.americano.domain.postcard.service.dto.response.SendPostcardResponse;

public interface PostcardService {
    SendPostcardResponse send(Long memberId, SendPostcardRequest sendPostcardRequest);
}
