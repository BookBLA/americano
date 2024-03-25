package com.bookbla.americano.domain.member.service.impl;

import com.bookbla.americano.domain.member.controller.dto.response.MemberLibraryProfileReadResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberTargetLibraryProfileReadResponse;
import com.bookbla.americano.domain.member.repository.*;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import com.bookbla.americano.domain.member.service.MemberLibraryService;
import java.util.List;

import com.bookbla.americano.domain.postcard.PostcardStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberLibraryServiceImpl implements MemberLibraryService {

    private final MemberRepository memberRepository;
    private final MemberProfileRepository memberProfileRepository;
    private final MemberBookRepository memberBookRepository;
    private final PostcardRepository PostcardRepository;

    @Override
    @Transactional(readOnly = true)
    public MemberLibraryProfileReadResponse getLibraryProfile(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberProfile memberProfile = memberProfileRepository.getByMemberOrThrow(member);
        List<MemberBook> memberBooks = memberBookRepository.findByMember(member);

        return MemberLibraryProfileReadResponse.of(member, memberProfile, memberBooks);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberTargetLibraryProfileReadResponse getTargetLibraryProfile(Long memberId, Long targetMemberId) {
        Member targetMember = memberRepository.getByIdOrThrow(targetMemberId);
        MemberProfile memberProfile = memberProfileRepository.getByMemberOrThrow(targetMember);
        List<MemberBook> memberBooks = memberBookRepository.findByMember(targetMember);

        boolean isMatched = false;

        if (PostcardRepository.existsBySendMemberIdAndReciveMemberIdAndPostcardStatus(memberId, targetMemberId, PostcardStatus.ACCEPT)) {
            isMatched = true;
        } else if (PostcardRepository.existsBySendMemberIdAndReciveMemberIdAndPostcardStatus(targetMemberId, memberId, PostcardStatus.ACCEPT)) {
            isMatched = true;
        }

        return MemberTargetLibraryProfileReadResponse.of(targetMember, memberProfile, memberBooks, isMatched);
    }
}
