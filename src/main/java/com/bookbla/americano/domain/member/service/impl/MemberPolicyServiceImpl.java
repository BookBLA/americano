package com.bookbla.americano.domain.member.service.impl;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.controller.dto.request.MailSendRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MailSendRequest.AgreedStatuses;
import com.bookbla.americano.domain.member.controller.dto.request.MemberPolicyUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberPolicyUpdateRequest.UpdateStatuses;
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
import java.lang.reflect.Field;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Update;
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
    public void createMemberPolicies(Long memberId, MailSendRequest mailSendRequest) {

        Member member = memberRepository.getByIdOrThrow(memberId);
        AgreedStatuses agreedStatuses = mailSendRequest.getAgreedStatuses();

        Policy policy = policyRepository.findById(1L)
            .orElseThrow(() -> new BaseException(PolicyExceptionType.NOT_EQUAL_POLICY_COUNT));

        MemberPolicyDto memberPolicyDto = new MemberPolicyDto(
            agreedStatuses.getAdAgreementPolicy());
        memberPolicyRepository.save(memberPolicyDto.toEntity(member, policy));
    }

    @Override
    public MemberPolicyResponse readMemberPolicies(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);

        Policy policy = policyRepository.findById(1L)
            .orElseThrow(() -> new BaseException(PolicyExceptionType.NOT_EQUAL_POLICY_COUNT));

        MemberPolicy memberPolicy = memberPolicyRepository.findByMemberAndPolicy(member, policy)
            .orElseThrow(() -> new IllegalArgumentException("Not found member_id"));

        return MemberPolicyResponse.from(member, memberPolicy);
    }

    @Override
    @Transactional
    public MemberPolicyResponse updateMemberPolicies(Long memberId,
        MemberPolicyUpdateRequest memberPolicyUpdateRequest) {

        Member member = memberRepository.getByIdOrThrow(memberId);
        UpdateStatuses updateStatuses = memberPolicyUpdateRequest.getUpdateStatuses();

        Policy policy = policyRepository.findById(1L)
            .orElseThrow(() -> new BaseException(PolicyExceptionType.NOT_EQUAL_POLICY_COUNT));

        MemberPolicy memberPolicy = memberPolicyRepository.findByMemberAndPolicy(member, policy)
            .orElseThrow(() -> new IllegalArgumentException("Not found member_id"));

        memberPolicy.updateAgreedStatus(updateStatuses.isAdAgreementPolicy());

        return MemberPolicyResponse.from(member, memberPolicy);
    }


}
