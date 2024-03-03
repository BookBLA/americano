package com.bookbla.americano.domain.member.controller;

import com.bookbla.americano.base.jwt.LoginUser;
import com.bookbla.americano.domain.member.controller.dto.request.MemberPolicyUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberPolicyResponse;
import com.bookbla.americano.domain.member.service.MemberPolicyService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/members/policies")
@RequiredArgsConstructor
public class MemberPolicyController {

    private final MemberPolicyService memberPolicyService;

    @PutMapping
    public ResponseEntity<List<MemberPolicyResponse>> updateMemberPolices(
        @RequestBody @Valid List<MemberPolicyUpdateRequest> memberPolicyUpdateRequests,
        @LoginUser Long memberId) {

        List<MemberPolicyResponse> memberPolicyResponses = memberPolicyService.updateMemberPolicies(
            memberId, memberPolicyUpdateRequests);

        return ResponseEntity.ok(memberPolicyResponses);
    }

}
