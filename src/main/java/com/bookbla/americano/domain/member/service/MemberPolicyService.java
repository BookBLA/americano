package com.bookbla.americano.domain.member.service;

import java.time.LocalDateTime;

import com.bookbla.americano.domain.member.controller.dto.request.MemberPolicyUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberPolicyResponse;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberPolicy;
import com.bookbla.americano.domain.member.service.dto.MemberPolicyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberPolicyService {

    private final MemberRepository memberRepository;

    @Transactional
    public MemberPolicyResponse createMemberPolicies(Long memberId, MemberPolicyDto memberPolicyDto) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        member.updateMemberPolicy(memberPolicyDto.toEntity());

        MemberPolicy memberPolicy = member.getMemberPolicy();

        return MemberPolicyResponse.from(member, memberPolicy);
    }

    public MemberPolicyResponse readMemberPolicies(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberPolicy memberPolicy = member.getMemberPolicy();

        return MemberPolicyResponse.from(member, memberPolicy);
    }

    @Transactional
    public MemberPolicyResponse updateMemberPolicies(
            Long memberId, MemberPolicyUpdateRequest memberPolicyUpdateRequest
    ) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberPolicy memberPolicy = member.getMemberPolicy();

        memberPolicy.updateAdAgreementPolicy(memberPolicyUpdateRequest.getAgreedStatuses().getAdAgreementPolicy())
                .updateAdAgreementAt(LocalDateTime.now());

        return MemberPolicyResponse.from(member, memberPolicy);
    }
}
