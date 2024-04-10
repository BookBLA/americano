package com.bookbla.americano.domain.member.service.impl;

import com.bookbla.americano.domain.member.controller.dto.response.MemberLibraryProfileReadResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberTargetLibraryProfileReadResponse;
import com.bookbla.americano.domain.member.repository.*;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import com.bookbla.americano.domain.member.service.MemberLibraryService;
import java.util.List;

import com.bookbla.americano.domain.postcard.enums.PostcardStatus;
import com.bookbla.americano.domain.postcard.repository.PostcardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberLibraryServiceImpl implements MemberLibraryService {

    private final MemberRepository memberRepository;
    private final MemberBookRepository memberBookRepository;
    private final PostcardRepository postcardRepository;

    @Override
    @Transactional(readOnly = true)
    public MemberLibraryProfileReadResponse getLibraryProfile(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberProfile memberProfile = member.getMemberProfile();
        List<MemberBook> memberBooks = memberBookRepository.findByMemberOrderByCreatedAt(member);

        return MemberLibraryProfileReadResponse.of(member, memberProfile, memberBooks);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberTargetLibraryProfileReadResponse getTargetLibraryProfile(Long memberId, Long targetMemberId) {
        Member targetMember = memberRepository.getByIdOrThrow(targetMemberId);
        MemberProfile memberProfile = targetMember.getMemberProfile();
        List<MemberBook> memberBooks = memberBookRepository.findByMemberOrderByCreatedAt(targetMember);

        boolean isMatched = false;

        if (postcardRepository.existsBySendMemberIdAndReceiveMemberIdAndPostcardStatus(memberId, targetMemberId, PostcardStatus.ACCEPT)) {
            isMatched = true;
        } else if (postcardRepository.existsBySendMemberIdAndReceiveMemberIdAndPostcardStatus(targetMemberId, memberId, PostcardStatus.ACCEPT)) {
            isMatched = true;
        }

        return MemberTargetLibraryProfileReadResponse.of(targetMember, memberProfile, memberBooks, isMatched);
    }
}
