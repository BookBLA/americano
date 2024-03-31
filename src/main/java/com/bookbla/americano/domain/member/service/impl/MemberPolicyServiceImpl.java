package com.bookbla.americano.domain.member.service.impl;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.controller.dto.request.MemberPolicyUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberPolicyResponse;
import com.bookbla.americano.domain.member.exception.PolicyExceptionType;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberPolicy;
import com.bookbla.americano.domain.member.service.MemberPolicyService;
import com.bookbla.americano.domain.member.service.dto.MemberPolicyDto;


import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberPolicyServiceImpl implements MemberPolicyService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public MemberPolicyResponse createMemberPolicies(Long memberId,
        MemberPolicyDto memberPolicyDto) {

        Member member = memberRepository.getByIdOrThrow(memberId);

        member.updateMemberPolicy(memberPolicyDto.toEntity());

        MemberPolicy memberPolicy = member.getMemberPolicy();

        return MemberPolicyResponse.from(member, memberPolicy);
    }

    @Override
    public MemberPolicyResponse readMemberPolicies(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberPolicy memberPolicy = member.getMemberPolicy();

        return MemberPolicyResponse.from(member, memberPolicy);
    }

    @Override
    @Transactional
    public MemberPolicyResponse updateMemberPolicies(
        Long memberId, MemberPolicyUpdateRequest memberPolicyUpdateRequest
    ) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberPolicy memberPolicy = member.getMemberPolicy();

        memberPolicy.updateAdAgreementPolicy(
            memberPolicyUpdateRequest.getAgreedStatuses().getAdAgreementPolicy())
            .updateAdAgreementAt(LocalDateTime.now());

        return MemberPolicyResponse.from(member, memberPolicy);
    }


}
