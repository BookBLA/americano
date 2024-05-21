package com.bookbla.americano.domain.postcard.service;

import com.bookbla.americano.domain.postcard.controller.dto.request.PostcardStatusUpdateRequest;
import com.bookbla.americano.domain.postcard.controller.dto.response.MemberPostcardFromResponse;
import com.bookbla.americano.domain.postcard.controller.dto.response.MemberPostcardToResponse;
import com.bookbla.americano.domain.postcard.controller.dto.response.PostcardSendValidateResponse;
import com.bookbla.americano.domain.postcard.service.dto.request.SendPostcardRequest;
import com.bookbla.americano.domain.postcard.service.dto.response.PostcardTypeResponse;
import com.bookbla.americano.domain.postcard.service.dto.response.SendPostcardResponse;

import java.util.List;

public interface PostcardService {
    SendPostcardResponse send(Long memberId, SendPostcardRequest sendPostcardRequest);

    PostcardTypeResponse getPostcardTypeList();

    List<MemberPostcardFromResponse> getPostcardsFromMember(Long memberId);

    List<MemberPostcardToResponse> getPostcardsToMember(Long memberId);

    void useMemberPostcard(Long memberId, String payType);

    void updatePostcardStatus(Long postcardId, PostcardStatusUpdateRequest request);

    PostcardSendValidateResponse validateSendPostcard(Long memberId, Long targetMemberId);
}
