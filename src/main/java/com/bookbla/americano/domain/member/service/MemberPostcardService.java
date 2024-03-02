package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.domain.member.controller.dto.request.MemberPostcardCountRequestDto;

public interface MemberPostcardService {
    int getMemberPostcardCount(MemberPostcardCountRequestDto memberPostcardCountRequestDto);
}
