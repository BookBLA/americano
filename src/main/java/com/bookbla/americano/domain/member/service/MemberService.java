package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.domain.member.controller.dto.request.MemberUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberResponse;
import com.bookbla.americano.domain.member.repository.entity.Member;

public interface MemberService {

    MemberResponse readMember(Long memberId);
    MemberResponse updateMember(Long memberId, MemberUpdateRequest memberUpdateRequest);
    Member getMemberById(Long memberId);
}
