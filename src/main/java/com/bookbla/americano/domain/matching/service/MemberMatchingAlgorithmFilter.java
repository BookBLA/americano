package com.bookbla.americano.domain.matching.service;

import com.bookbla.americano.domain.matching.repository.entity.MatchedInfo;
import com.bookbla.americano.domain.member.repository.MemberBookRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 알고리즘 우선순위
 *
 * 1. 같은 학교 같은책
 * 2. 다른 학교 같은 책
 * 3. 같은 학교 같은 작가
 * 4. 다른 학교 같은 작가
 * 5. 같은학교 흡연여부 동일
 * 6. 다른 학교 흡연여부 동일
 */
@Service
@RequiredArgsConstructor
public class MemberMatchingAlgorithmFilter {

    private final MemberRepository memberRepository;
    private final MemberBookRepository memberBookRepository;

    public void memberMatchingAlgorithmFiltering(Member member, List<MatchedInfo> matchingMembers) {

        for (MatchedInfo matchedInfo : matchingMembers) {
            Member matchingMember = memberRepository.getByIdOrThrow(matchedInfo.getMatchedMemberId());
            MemberBook matchingMemberBook = memberBookRepository.getByIdOrThrow(matchedInfo.getMatchedMemberBookId());

            if (isSameSchool(member, matchingMember) && isSameBook(member, matchingMemberBook)) {
                matchedInfo.accumulateSimilarityWeight(1.0);
            } else if (!isSameSchool(member, matchingMember) && isSameBook(member, matchingMemberBook)) {
                matchedInfo.accumulateSimilarityWeight(0.8);
            } else if (isSameSchool(member, matchingMember) && isSameAuthor(member, matchingMemberBook)) {
                matchedInfo.accumulateSimilarityWeight(0.6);
            } else if (!isSameSchool(member, matchingMember) && isSameAuthor(member, matchingMemberBook)) {
                matchedInfo.accumulateSimilarityWeight(0.4);
            } else if (isSameSchool(member, matchingMember) && isSameSmoking(member, matchingMember)) {
                matchedInfo.accumulateSimilarityWeight(0.2);
            } else if (!isSameSchool(member, matchingMember) && isSameSmoking(member, matchingMember)) {
                matchedInfo.accumulateSimilarityWeight(0.1);
            }
        }
    }

    private boolean isSameSchool(Member member, Member matchingMember) {
        return member.getSchool().equals(matchingMember.getSchool());
    }

    private boolean isSameBook(Member member, MemberBook matchingMemberBook) {
        List<MemberBook> memberBooks = memberBookRepository.findByMemberOrderByCreatedAt(member);

        for (MemberBook memberBook : memberBooks) {
            if (memberBook.getBook().equals(matchingMemberBook.getBook())) {
                return true;
            }
        }

        return false;
    }

    private boolean isSameAuthor(Member member, MemberBook matchingMemberBook) {
        List<MemberBook> memberBooks = memberBookRepository.findByMemberOrderByCreatedAt(member);

        for (MemberBook memberBook : memberBooks) {
            // 대표 저자로 확인
            if (memberBook.getBook().getAuthors().get(0).equals(matchingMemberBook.getBook().getAuthors().get(0))) {
                return true;
            }
        }

        return false;
    }

    private boolean isSameSmoking(Member member, Member matchingMember) {
        return member.getMemberStyle().getSmokeType() == matchingMember.getMemberStyle().getSmokeType();
    }
}
