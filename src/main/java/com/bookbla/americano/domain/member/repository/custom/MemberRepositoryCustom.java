package com.bookbla.americano.domain.member.repository.custom;

import com.bookbla.americano.domain.member.controller.dto.request.MemberBookProfileRequestDto;
import com.bookbla.americano.domain.member.controller.dto.response.BookProfileResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBookProfileResponse;

import java.util.List;

public interface MemberRepositoryCustom {
    List<MemberBookProfileResponse> searchSameBookMember(Long memberId, MemberBookProfileRequestDto requestDto);
    List<BookProfileResponse> getAllMembers(Long memberId, MemberBookProfileRequestDto requestDto);
}
