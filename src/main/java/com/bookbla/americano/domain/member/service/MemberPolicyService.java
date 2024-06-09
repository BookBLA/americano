package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.domain.member.controller.dto.request.MemberPolicyUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberPolicyResponse;
import com.bookbla.americano.domain.member.service.dto.MemberPolicyDto;

public interface MemberPolicyService {

    MemberPolicyResponse createMemberPolicies(Long memberId, MemberPolicyDto memberPolicyDto);

    MemberPolicyResponse readMemberPolicies(Long memberId);

    MemberPolicyResponse updateMemberPolicies(Long memberId,
        MemberPolicyUpdateRequest memberPolicyUpdateRequest);

}
