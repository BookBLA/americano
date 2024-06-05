package com.bookbla.americano.domain.library.service.impl;

import java.util.List;

import com.bookbla.americano.domain.library.service.MemberLibraryService;
import com.bookbla.americano.domain.library.controller.dto.MemberLibraryProfileReadResponse;
import com.bookbla.americano.domain.library.controller.dto.MemberTargetLibraryProfileReadResponse;
import com.bookbla.americano.domain.member.repository.MemberBookRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.MemberVerifyRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import com.bookbla.americano.domain.postcard.enums.PostcardStatus;
import com.bookbla.americano.domain.postcard.repository.PostcardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberLibraryServiceImpl implements MemberLibraryService {

    private final MemberRepository memberRepository;
    private final MemberVerifyRepository memberVerifyRepository;
    private final MemberBookRepository memberBookRepository;
    private final PostcardRepository postcardRepository;

    @Override
    public MemberLibraryProfileReadResponse getLibraryProfile(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        List<MemberBook> memberBooks = memberBookRepository.findByMemberOrderByCreatedAt(member);

        return memberVerifyRepository.findMemberPendingProfileImage(member.getId())
                .stream()
                .findFirst()
                .map(image -> MemberLibraryProfileReadResponse.ofPendingProfileImage(member, memberBooks, image))
                .orElseGet(() -> MemberLibraryProfileReadResponse.of(member, memberBooks));
    }

    @Override
    public MemberTargetLibraryProfileReadResponse getTargetLibraryProfile(Long memberId, Long targetMemberId) {
        Member targetMember = memberRepository.getByIdOrThrow(targetMemberId);
        List<MemberBook> memberBooks = memberBookRepository.findByMemberOrderByCreatedAt(targetMember);

        boolean isMatched = false;

        if (postcardRepository.existsBySendMemberIdAndReceiveMemberIdAndPostcardStatus(memberId, targetMemberId, PostcardStatus.ACCEPT)) {
            isMatched = true;
        } else if (postcardRepository.existsBySendMemberIdAndReceiveMemberIdAndPostcardStatus(targetMemberId, memberId, PostcardStatus.ACCEPT)) {
            isMatched = true;
        }

        return MemberTargetLibraryProfileReadResponse.of(targetMember, memberBooks, isMatched);
    }
}
