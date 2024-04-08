package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.domain.member.controller.dto.request.MemberUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberStatusResponse;
import com.bookbla.americano.domain.member.service.dto.MemberProfileDto;

public interface MemberService {

    MemberResponse createMember(Long memberSignUpInformationId,MemberProfileDto memberProfileDto);

    MemberResponse readMember(Long memberId);

    MemberStatusResponse readMemberStatus(Long memberId);

    MemberResponse updateMember(Long memberId, MemberUpdateRequest memberUpdateRequest);
}
