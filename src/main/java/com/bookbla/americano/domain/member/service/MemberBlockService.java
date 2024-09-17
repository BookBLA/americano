package com.bookbla.americano.domain.member.service;

import java.util.List;
import java.util.Objects;

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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberBlockService {

    private final MemberRepository memberRepository;
    private final MemberBlockRepository memberBlockRepository;

    @Transactional
    public MemberBlockCreateResponse addMemberBlock(Long memberId,
                                                    MemberBlockCreateRequest memberBlockCreateRequest) {

        if (Objects.equals(memberId, memberBlockCreateRequest.getBlockedMemberId())) {
            throw new BaseException(MemberBlockExceptionType.SAME_MEMBER);
        }

        // 차단을 하는 멤버
        Member blockerMember = memberRepository.getByIdOrThrow(memberId);

        // 차단을 당하는 멤버
        Member blockedMember = memberRepository.getByIdOrThrow(
                memberBlockCreateRequest.getBlockedMemberId());

        if (memberBlockRepository.findByBlockerMemberAndBlockedMember(blockerMember, blockedMember).isPresent()) {
            throw new BaseException(MemberBlockExceptionType.ALREADY_MEMBER_BLOCK);
        }

        MemberBlock memberBlock = MemberBlock.builder()
                .blockerMember(blockerMember)
                .blockedMember(blockedMember)
                .build();

        memberBlockRepository.save(memberBlock);
        return MemberBlockCreateResponse.from(memberBlock);
    }

    @Transactional(readOnly = true)
    public MemberBlockReadResponse readMemberBlock(Long memberId) {

        // 차단을 하는 멤버
        Member blockerMember = memberRepository.getByIdOrThrow(memberId);

        List<MemberBlock> memberBlocks = memberBlockRepository.findAllByBlockerMember(blockerMember);
        return MemberBlockReadResponse.from(memberBlocks);
    }

    @Transactional
    public MemberBlockDeleteResponse deleteMemberBlock(Long memberId, Long memberBlockId) {

        MemberBlock memberBlock = memberBlockRepository.findById(memberBlockId)
                .orElse(null);

        if (memberBlock == null) {
            return MemberBlockDeleteResponse.from(memberBlockId);
        }

        Member blockerMember = memberRepository.getByIdOrThrow(memberId);

        if (!memberBlock.getBlockerMember().equals(blockerMember)) {
            throw new BaseException(MemberBlockExceptionType.NOT_SAME_MEMBER_BLOCK);
        }

        memberBlockRepository.delete(memberBlock);
        return MemberBlockDeleteResponse.from(memberBlockId);

        //*
        //
        // get - /members/me/admob
        //post - /members/me/admob
        //이 두가지 api 모두 parameter를 던져서 어떤 애드몹인지 확인해야 합니다
        //
        //예시
        //export const getReloadAdmobCount = async (what: string) => {
        //  const res = await Get('members/me/admob', {admobType: what});
        //  // @ts-ignore
        //  return res.result.admobCount;
        //};
        //
        //admobType에 'NEWPERSON', 'FREEBOOKMARK' 값을 전달해서 보내면 어떤 타입인지 확인해서 각 상황에 맞는 작업을 하면 될거같아요.
        //
        //'NEWPERSON' - 새로운 사람 찾기
        //이건 admou_count만 줄여주시면 될 것 같아요. 사람 돌리는건 화면에서 다 되니깐
        //혹시 뭔가 놓친게 있다면 알려주세요
        //'FREEBOOKMARK' - 매일 무료 책갈피
        //post요청시에 책갈피 10개 지급해주셔야 합니다.
        //
        // /
    }
}
