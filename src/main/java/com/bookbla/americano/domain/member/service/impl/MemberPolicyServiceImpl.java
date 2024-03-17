package com.bookbla.americano.domain.member.service.impl;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.controller.dto.request.MemberPolicyUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberPolicyResponse;
import com.bookbla.americano.domain.member.exception.PolicyExceptionType;
import com.bookbla.americano.domain.member.repository.MemberPolicyRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.PolicyRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberPolicy;
import com.bookbla.americano.domain.member.repository.entity.Policy;
import com.bookbla.americano.domain.member.service.MemberPolicyService;
import com.bookbla.americano.domain.member.service.dto.MemberPolicyDto;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberPolicyServiceImpl implements MemberPolicyService {

    private final MemberRepository memberRepository;
    private final PolicyRepository policyRepository;
    private final MemberPolicyRepository memberPolicyRepository;

    @Override
    @Transactional
    public MemberPolicyResponse createMemberPolicies(Long memberId, MemberPolicyDto memberPolicyDto) {

        Member member = memberRepository.getByIdOrThrow(memberId);

        List<Policy> policies = policyRepository.findAllSortedById();
        List<Boolean> agreedStatuses = memberPolicyDto.getAgreedStatuses();


        List<MemberPolicy> memberPolicies = new ArrayList<>();
        for (int i = 0; i < policies.size(); i++) {
            MemberPolicy memberPolicy = memberPolicyDto.toEntity(member, policies.get(i), agreedStatuses.get(i));
            memberPolicies.add(memberPolicy);
            memberPolicyRepository.save(memberPolicy);
        }

        return MemberPolicyResponse.from(member, memberPolicies);
    }

    @Override
    public MemberPolicyResponse readMemberPolicies(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);

        List<MemberPolicy> memberPolicies = memberPolicyRepository.findAllByMember(member);
        if(memberPolicies.isEmpty()) {
            throw new BaseException(PolicyExceptionType.MEMBER_NOT_REGISTERED);
        }


        return MemberPolicyResponse.from(member, memberPolicies);
    }

    @Override
    @Transactional
    public MemberPolicyResponse updateMemberPolicies(Long memberId,
        MemberPolicyUpdateRequest memberPolicyUpdateRequest) {

        Member member = memberRepository.getByIdOrThrow(memberId);

        List<Policy> policies = policyRepository.findAllSortedById();
        List<Boolean> agreedStatuses = memberPolicyUpdateRequest.getUpdateStatuses().toList();

        for (int i = 0; i < policies.size(); i++) {
            MemberPolicy memberPolicy = memberPolicyRepository.findByMemberAndPolicy(member,
                policies.get(i)).orElseThrow(
                () -> new BaseException(PolicyExceptionType.EACH_POLICY_NOT_REGISTERED));

            memberPolicy.updateAgreedStatus(agreedStatuses.get(i));
        }

        List<MemberPolicy> memberPolicies = memberPolicyRepository.findAllByMember(member);
        if(memberPolicies.isEmpty()) {
            throw new BaseException(PolicyExceptionType.MEMBER_NOT_REGISTERED);
        }

        return MemberPolicyResponse.from(member, memberPolicies);
    }


}
