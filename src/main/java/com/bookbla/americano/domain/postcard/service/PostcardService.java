package com.bookbla.americano.domain.postcard.service;

import com.bookbla.americano.domain.postcard.controller.dto.response.ContactInfoResponse;
import com.bookbla.americano.domain.postcard.controller.dto.response.MemberPostcardFromResponse;
import com.bookbla.americano.domain.postcard.controller.dto.response.MemberPostcardToResponse;
import com.bookbla.americano.domain.postcard.controller.dto.response.PostcardSendValidateResponse;
import com.bookbla.americano.domain.postcard.enums.PostcardStatus;
import com.bookbla.americano.domain.postcard.service.dto.request.SendPostcardRequest;
import com.bookbla.americano.domain.postcard.service.dto.response.PostcardTypeResponse;
import com.bookbla.americano.domain.postcard.service.dto.response.SendPostcardResponse;
import java.util.List;

public interface PostcardService {

    SendPostcardResponse send(Long memberId, SendPostcardRequest sendPostcardRequest);

    PostcardTypeResponse getPostcardTypeList();

    List<MemberPostcardFromResponse> getPostcardsFromMember(Long memberId);

    List<MemberPostcardToResponse> getPostcardsToMember(Long memberId);

    void readMemberPostcard(Long memberId, Long postcardId);

    void updatePostcardStatus(Long memberId, Long postcardId, PostcardStatus postcardStatus);

    PostcardStatus getPostcardStatus(Long postcardId);

    PostcardSendValidateResponse validateSendPostcard(Long memberId, Long targetMemberId);

    ContactInfoResponse getContactInfo(Long memberId, Long postcardId);
}
