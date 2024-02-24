package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.domain.member.controller.dto.request.MemberBookProfileRequestDto;
import com.bookbla.americano.domain.member.controller.dto.request.MemberCoinCountRequestDto;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBookProfileResponseDto;
import com.bookbla.americano.domain.member.controller.dto.request.MemberUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberResponse;
import com.bookbla.americano.domain.member.repository.entity.Member;

import java.util.List;

public interface MemberService {

    int getMemberCoinCount(MemberCoinCountRequestDto memberCoinCountRequestDto);
    MemberResponse readMember(Long memberId);
    MemberResponse updateMember(Long memberId, MemberUpdateRequest memberUpdateRequest);
    List<MemberBookProfileResponseDto> findSameBookMembers(MemberBookProfileRequestDto memberBookProfileRequestDto);
    Member getMemberById(Long memberId);
}
