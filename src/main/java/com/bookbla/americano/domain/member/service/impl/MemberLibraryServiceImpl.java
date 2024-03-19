package com.bookbla.americano.domain.member.service.impl;

import com.bookbla.americano.domain.member.controller.dto.response.MemberLibraryProfileReadResponse;
import com.bookbla.americano.domain.member.repository.MemberBookRepository;
import com.bookbla.americano.domain.member.repository.MemberProfileRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import com.bookbla.americano.domain.member.service.MemberLibraryService;
import java.util.List;
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
    public MemberLibraryProfileReadResponse getLibraryProfile(Long memberId, Long targetMemberId) {
        Member targetMember = memberRepository.getByIdOrThrow(targetMemberId);
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberProfile memberProfile = memberProfileRepository.getByMemberOrThrow(targetMember);
        List<MemberBook> memberBooks = memberBookRepository.findByMember(targetMember);

        // TODO: 매칭 여부 확인해서 isMatched 값 설정
        boolean isMathced = false;

        return MemberLibraryProfileReadResponse.of(targetMember, memberProfile, memberBooks, isMathced);
    }
}
