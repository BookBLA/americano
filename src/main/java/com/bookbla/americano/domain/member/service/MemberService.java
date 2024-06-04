package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.domain.member.controller.dto.response.MemberDeleteResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberStatusResponse;
import com.bookbla.americano.domain.member.enums.MemberStatus;

public interface MemberService {

    MemberResponse readMember(Long memberId);

    MemberStatusResponse readMemberStatus(Long memberId);

    MemberDeleteResponse deleteMember(Long memberId);

    MemberStatusResponse updateStatus(Long memberId, MemberStatus memberStatus, String reason);
}
