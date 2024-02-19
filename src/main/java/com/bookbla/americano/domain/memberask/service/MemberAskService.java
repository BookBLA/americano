package com.bookbla.americano.domain.memberask.service;

import com.bookbla.americano.domain.member.Member;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.memberask.controller.dto.request.MemberAskCreateRequest;
import com.bookbla.americano.domain.memberask.controller.dto.request.MemberAskUpdateRequest;
import com.bookbla.americano.domain.memberask.controller.dto.response.MemberAskResponse;
import com.bookbla.americano.domain.memberask.repository.MemberAskRepository;
import com.bookbla.americano.domain.memberask.repository.entity.MemberAsk;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberAskService {

    private final MemberAskRepository memberAskRepository;
    private final MemberRepository memberRepository;

    public MemberAskResponse createMemberAsk(Long memberId, MemberAskCreateRequest memberAskCreateRequest) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberAsk memberAsk = memberAskCreateRequest.toMemberAskWith(member);
        MemberAsk savedMemberAsk = memberAskRepository.save(memberAsk);
        return MemberAskResponse.from(savedMemberAsk);
    }

    @Transactional(readOnly = true)
    public MemberAskResponse readMemberAsk(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberAsk memberAsk = memberAskRepository.getByMemberOrThrow(member);
        return MemberAskResponse.from(memberAsk);
    }

    public void updateMemberAsk(Long memberId, MemberAskUpdateRequest memberAskUpdateRequest) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberAsk memberAsk = memberAskRepository.getByMemberOrThrow(member);

        memberAsk.updateContent(memberAskUpdateRequest.getContents());
    }

}
