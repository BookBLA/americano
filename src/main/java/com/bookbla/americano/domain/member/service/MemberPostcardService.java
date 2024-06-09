package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.domain.postcard.controller.dto.response.MemberPostcardResponse;

public interface MemberPostcardService {
    int getMemberPostcardCount(Long memberId);

    MemberPostcardResponse getMemberPostcardEachCount(Long memberId);
}
