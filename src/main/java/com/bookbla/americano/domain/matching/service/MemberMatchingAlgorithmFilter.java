package com.bookbla.americano.domain.matching.service;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.matching.exception.MemberMatchingExceptionType;
import com.bookbla.americano.domain.matching.repository.entity.MatchedInfo;
import com.bookbla.americano.domain.member.repository.MemberBookRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.bookbla.americano.domain.matching.service.dto.MatchingSimilarityWeightConstants.*;

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
@Transactional
@RequiredArgsConstructor
public class MemberMatchingAlgorithmFilter {

    private final MemberRepository memberRepository;
    private final MemberBookRepository memberBookRepository;

    public void memberMatchingAlgorithmFiltering(Member member, List<MatchedInfo> matchingMembers) {
        List<MemberBook> memberBooks = memberBookRepository.findByMemberOrderByCreatedAt(member);
        List<Member> matchingMemberList = memberRepository.findAllById(matchingMembers.stream()
                .map(MatchedInfo::getMatchedMemberId)
                .collect(Collectors.toList()));

        Map<Long, MemberBook> memberBookMap = memberBookRepository.findAllById(matchingMembers.stream()
                        .map(MatchedInfo::getMatchedMemberBookId)
                        .collect(Collectors.toList()))
                .stream()
                .collect(Collectors.toMap(MemberBook::getId, book -> book));

        for (MatchedInfo matchedInfo : matchingMembers) {
            Member matchingMember = matchingMemberList.stream()
                    .filter(m -> m.getId().equals(matchedInfo.getMatchedMemberId()))
                    .findFirst().orElseThrow(() -> new BaseException(MemberMatchingExceptionType.MATCHING_MEMBER_DOESNT_EXIST));
            MemberBook matchingMemberBook = memberBookMap.get(matchedInfo.getMatchedMemberBookId());

            if (isSameSchool(member, matchingMember) && isSameBook(memberBooks, matchingMemberBook)) {
                matchedInfo.accumulateSimilarityWeight(SAME_SCHOOL_SAME_BOOK);
            } else if (!isSameSchool(member, matchingMember) && isSameBook(memberBooks, matchingMemberBook)) {
                matchedInfo.accumulateSimilarityWeight(OTHER_SCHOOL_SAME_BOOK);
            } else if (isSameSchool(member, matchingMember) && isSameAuthor(memberBooks, matchingMemberBook)) {
                matchedInfo.accumulateSimilarityWeight(SAME_SCHOOL_SAME_AUTHOR);
            } else if (!isSameSchool(member, matchingMember) && isSameAuthor(memberBooks, matchingMemberBook)) {
                matchedInfo.accumulateSimilarityWeight(OTHER_SCHOOL_SAME_AUTHOR);
            } else if (isSameSchool(member, matchingMember) && isSameSmoking(member, matchingMember)) {
                matchedInfo.accumulateSimilarityWeight(SAME_SCHOOL_SAME_SMOKING);
            } else if (!isSameSchool(member, matchingMember) && isSameSmoking(member, matchingMember)) {
                matchedInfo.accumulateSimilarityWeight(OTHER_SCHOOL_SAME_SMOKING);
            } else if (isSameSchool(member, matchingMember)) {
                matchedInfo.accumulateSimilarityWeight(SAME_SCHOOL);
            } else if (isSameBook(memberBooks, matchingMemberBook)) {
                matchedInfo.accumulateSimilarityWeight(SAME_BOOK);
            }
        }
    }

    private boolean isSameBook(List<MemberBook> memberBooks, MemberBook matchingMemberBook) {
        if (memberBooks == null || matchingMemberBook == null || matchingMemberBook.getBook() == null) {
            return false;
        }
        return memberBooks.stream().anyMatch(book -> book.getBook().equals(matchingMemberBook.getBook()));
    }

    private boolean isSameAuthor(List<MemberBook> memberBooks, MemberBook matchingMemberBook) {
        return memberBooks.stream().anyMatch(book -> !book.getBook().getAuthors().isEmpty() &&
                !matchingMemberBook.getBook().getAuthors().isEmpty() &&
                book.getBook().getAuthors().get(0).equals(matchingMemberBook.getBook().getAuthors().get(0)));
    }

    private boolean isSameSchool(Member member, Member matchingMember) {
        return member.getSchool().equals(matchingMember.getSchool());
    }

    private boolean isSameSmoking(Member member, Member matchingMember) {
        return member.getMemberStyle().getSmokeType() == matchingMember.getMemberStyle().getSmokeType();
    }
}
