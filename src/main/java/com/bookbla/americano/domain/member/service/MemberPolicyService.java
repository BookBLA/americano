package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.domain.member.controller.dto.request.MemberPolicyCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberPolicyUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberPolicyResponse;
import java.util.List;

public interface MemberPolicyService {

    List<MemberPolicyResponse> createMemberPolicies(Long memberId,
        List<MemberPolicyCreateRequest> memberPolicyCreateRequests);

    List<MemberPolicyResponse> updateMemberPolicies(Long memberId,
        List<MemberPolicyUpdateRequest> memberPolicyUpdateRequests);

}
