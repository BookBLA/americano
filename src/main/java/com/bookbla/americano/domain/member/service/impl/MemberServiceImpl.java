package com.bookbla.americano.domain.member.service.impl;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.controller.dto.request.MemberUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberDeleteResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberStatusResponse;
import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.service.MemberService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public MemberResponse readMember(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        return MemberResponse.from(member);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberStatusResponse readMemberStatus(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        return MemberStatusResponse.from(member);
    }

    @Override
    @Transactional
    public MemberResponse updateMember(Long memberId, MemberUpdateRequest memberUpdateRequest) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        update(member, memberUpdateRequest);

        return MemberResponse.from(member);
    }

    @Override
    @Transactional
    public MemberDeleteResponse deleteMember(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);

        if (member.getMemberStatus() == MemberStatus.DELETED) {
            throw new BaseException(MemberExceptionType.MEMBER_STATUS_NOT_VALID);
        }
        member.updateDeleteAt(LocalDateTime.now())
                .updateMemberStatus(MemberStatus.DELETED, LocalDateTime.now());

        return MemberDeleteResponse.from(member);
    }

    private void update(Member member, MemberUpdateRequest request) {
        member.updateOauthEmail(request.getOauthEmail())
                .updateMemberType(request.getMemberType())
                .updateMemberStatus(request.getMemberStatus(), LocalDateTime.now())
                .updateMemberType(request.getMemberType());
    }
}