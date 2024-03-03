package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.domain.member.controller.dto.request.MailSendRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberPolicyUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberPolicyResponse;
import com.bookbla.americano.domain.member.service.dto.MemberPolicyDto;
import java.util.List;

public interface MemberPolicyService {

    void createMemberPolicies(Long memberId, MailSendRequest mailSendRequest);

    List<MemberPolicyResponse> updateMemberPolicies(Long memberId,
        List<MemberPolicyUpdateRequest> memberPolicyUpdateRequests);

}
