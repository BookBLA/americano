package com.bookbla.americano.view.library.service;

import java.util.List;

import com.bookbla.americano.view.library.controller.dto.MyLibraryReadResponse;
import com.bookbla.americano.view.library.controller.dto.OtherLibraryReadResponse;
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
public class LibraryService {

    private final MemberRepository memberRepository;
    private final MemberVerifyRepository memberVerifyRepository;
    private final MemberBookRepository memberBookRepository;
    private final PostcardRepository postcardRepository;

    public MyLibraryReadResponse getLibraryProfile(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        List<MemberBook> memberBooks = memberBookRepository.findByMemberOrderByCreatedAt(member);

        return memberVerifyRepository.findMemberPendingProfileImage(member.getId())
                .stream()
                .findFirst()
                .map(image -> MyLibraryReadResponse.ofPendingProfileImage(member, memberBooks, image))
                .orElseGet(() -> MyLibraryReadResponse.of(member, memberBooks));
    }

    public OtherLibraryReadResponse getTargetLibraryProfile(Long memberId, Long targetMemberId) {
        Member targetMember = memberRepository.getByIdOrThrow(targetMemberId);
        List<MemberBook> memberBooks = memberBookRepository.findByMemberOrderByCreatedAt(targetMember);

        boolean isMatched = false;

        if (postcardRepository.existsBySendMemberIdAndReceiveMemberIdAndPostcardStatus(memberId, targetMemberId, PostcardStatus.ACCEPT)) {
            isMatched = true;
        } else if (postcardRepository.existsBySendMemberIdAndReceiveMemberIdAndPostcardStatus(targetMemberId, memberId, PostcardStatus.ACCEPT)) {
            isMatched = true;
        }

        return OtherLibraryReadResponse.of(targetMember, memberBooks, isMatched);
    }
}
