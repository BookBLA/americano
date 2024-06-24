package com.bookbla.americano.domain.member.service;

import java.time.LocalDateTime;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.controller.dto.response.MemberDeleteResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberStatusResponse;
import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.MemberStatusLogRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberStatusLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberStatusLogRepository memberStatusLogRepository;

    @Transactional(readOnly = true)
    public MemberResponse readMember(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        return MemberResponse.from(member);
    }

    @Transactional(readOnly = true)
    public MemberStatusResponse readMemberStatus(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        return MemberStatusResponse.from(member);
    }

    @Transactional
    public MemberDeleteResponse deleteMember(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);

        if (member.getMemberStatus() == MemberStatus.DELETED) {
            throw new BaseException(MemberExceptionType.MEMBER_STATUS_NOT_VALID);
        }
        memberStatusLogRepository.save(
                MemberStatusLog.builder()
                        .memberId(member.getId())
                        .beforeStatus(member.getMemberStatus())
                        .afterStatus(MemberStatus.DELETED)
                        .build()
        );
        member.updateDeleteAt(LocalDateTime.now())
                .updateMemberStatus(MemberStatus.DELETED, LocalDateTime.now());

        return MemberDeleteResponse.from(member);
    }

    @Transactional
    public MemberStatusResponse updateStatus(Long memberId,
                                             MemberStatus memberStatus,
                                             String reason) {
        Member member = memberRepository.getByIdOrThrow(memberId);

        MemberStatusLog.MemberStatusLogBuilder memberStatusLogBuilder = MemberStatusLog.builder()
                .memberId(memberId)
                .beforeStatus(member.getMemberStatus())
                .afterStatus(memberStatus);

        if (memberStatus.isMatchingDisabled()) {
            memberStatusLogBuilder.description(reason);
        }

        memberStatusLogRepository.save(memberStatusLogBuilder.build());

        member.updateMemberStatus(memberStatus, LocalDateTime.now());

        return MemberStatusResponse.from(member);
    }
}