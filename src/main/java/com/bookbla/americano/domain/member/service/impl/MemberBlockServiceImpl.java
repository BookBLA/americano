package com.bookbla.americano.domain.member.service.impl;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.controller.dto.request.MemberBlockCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBlockCreateResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBlockDeleteResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBlockReadResponse;
import com.bookbla.americano.domain.member.exception.MemberBlockExceptionType;
import com.bookbla.americano.domain.member.repository.MemberBlockRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBlock;
import com.bookbla.americano.domain.member.service.MemberBlockService;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberBlockServiceImpl implements MemberBlockService {

    private final MemberRepository memberRepository;
    private final MemberBlockRepository memberBlockRepository;

    @Override
    @Transactional
    public MemberBlockCreateResponse addMemberBlock(Long memberId,
        MemberBlockCreateRequest memberBlockCreateRequest) {

        if (Objects.equals(memberId, memberBlockCreateRequest.getBlockedByMemberId())) {
            throw new BaseException(MemberBlockExceptionType.SAME_MEMBER);
        }

        // 차단을 하는 멤버
        Member blockerMember = memberRepository.getByIdOrThrow(memberId);

        // 차단을 당하는 멤버
        Member blockedByMember = memberRepository.getByIdOrThrow(
            memberBlockCreateRequest.getBlockedByMemberId());

        if (memberBlockRepository.findByBlockerMemberAndBlockedByMember(blockerMember, blockedByMember).isPresent()) {
            throw new BaseException(MemberBlockExceptionType.ALREADY_MEMBER_BLOCK);
        }

        MemberBlock memberBlock = MemberBlock.builder()
            .blockerMember(blockerMember)
            .blockedByMember(blockedByMember)
            .build();

        memberBlockRepository.save(memberBlock);
        return MemberBlockCreateResponse.from(memberBlock, blockerMember, blockedByMember);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberBlockReadResponse readMemberBlock(Long memberId) {

        // 차단을 하는 멤버
        Member blockerMember = memberRepository.getByIdOrThrow(memberId);

        List<MemberBlock> memberBlocks = memberBlockRepository.findAllByBlockerMember(blockerMember);
        return MemberBlockReadResponse.from(memberBlocks);
    }

    @Override
    @Transactional
    public MemberBlockDeleteResponse deleteMemberBlock(Long memberId, Long memberBlockId) {

        MemberBlock memberBlock = memberBlockRepository.findById(memberBlockId)
            .orElseThrow(() -> new BaseException(MemberBlockExceptionType.NOT_FOUND_MEMBER_BLOCK));

        Member blockerMember = memberRepository.getByIdOrThrow(memberId);

        if (!memberBlock.getBlockerMember().equals(blockerMember)) {
            throw new BaseException(MemberBlockExceptionType.NOT_SAME_MEMBER_BLOCK);
        }

        memberBlockRepository.delete(memberBlock);
        return MemberBlockDeleteResponse.from(memberBlockId, blockerMember);
    }
}
