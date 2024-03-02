package com.bookbla.americano.domain.member.repository.custom;

import com.bookbla.americano.domain.member.controller.dto.request.MemberBookProfileRequestDto;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBookProfileResponseDto;

import java.util.List;

public interface MemberProfileRepositoryCustom {
    List<MemberBookProfileResponseDto> searchSameBookMember(MemberBookProfileRequestDto requestDto);
}
