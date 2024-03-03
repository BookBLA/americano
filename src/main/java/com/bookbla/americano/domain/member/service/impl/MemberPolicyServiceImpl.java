package com.bookbla.americano.domain.member.service.impl;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.controller.dto.request.MemberPolicyCreateRequest;
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
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
    public List<MemberPolicyResponse> createMemberPolicies(Long memberId,
        List<MemberPolicyCreateRequest> memberPolicyCreateRequests) {

        Member member = memberRepository.getByIdOrThrow(memberId);
        List<Policy> policies = policyRepository.findAll(Sort.by("id"));

        if (memberPolicyCreateRequests.size() != policies.size()) {
            throw new BaseException(PolicyExceptionType.NOT_EQUAL_POLICY_COUNT);
        }

        return IntStream.range(0, policies.size())
            .mapToObj(i -> new SimpleEntry<>(memberPolicyCreateRequests.get(i), policies.get(i)))
            .map(entry -> {
                MemberPolicyCreateRequest request = entry.getKey();
                Policy policy = entry.getValue();
                MemberPolicyDto memberPolicyDto = request.toDto(member, policy);
                return memberPolicyRepository.save(memberPolicyDto.toEntity());
            })
            .map(MemberPolicyResponse::from)
            .collect(Collectors.toList());

    }

    @Override
    @Transactional
    public List<MemberPolicyResponse> updateMemberPolicies(Long memberId,
        List<MemberPolicyUpdateRequest> memberPolicyUpdateRequests) {

        Member member = memberRepository.getByIdOrThrow(memberId);
        List<Policy> policies = policyRepository.findAll(Sort.by("id"));

        if (memberPolicyUpdateRequests.size() != policies.size()) {
            throw new BaseException(PolicyExceptionType.NOT_EQUAL_POLICY_COUNT);
        }

        return IntStream.range(0, policies.size())
            .mapToObj(i -> new SimpleEntry<>(memberPolicyUpdateRequests.get(i), policies.get(i)))
            .map(entry -> {
                MemberPolicyUpdateRequest request = entry.getKey();
                Policy policy = entry.getValue();
                MemberPolicy memberPolicy = memberPolicyRepository.findByMemberAndPolicy(member, policy)
                    .orElseThrow(() -> new IllegalArgumentException("Error"));
                memberPolicy.updateAgreedStatus(request.getAgreedStatus());
                return memberPolicy;
            })
            .map(MemberPolicyResponse::from)
            .collect(Collectors.toList());
    }

}