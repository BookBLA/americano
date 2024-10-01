package com.bookbla.americano.domain.matching.service;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.matching.exception.MemberMatchingExceptionType;
import com.bookbla.americano.domain.matching.repository.entity.MatchedInfo;
import com.bookbla.americano.domain.member.repository.MemberBookRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.bookbla.americano.domain.matching.service.dto.MatchingSimilarityWeightConstants.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberMatchingAlgorithmFilter {

    private final MemberRepository memberRepository;
    private final MemberBookRepository memberBookRepository;

    public List<MatchedInfo> memberMatchingAlgorithmFiltering(Member member, List<MatchedInfo> matchingMembers) {
        List<MemberBook> memberBooks = memberBookRepository.findByMemberOrderByCreatedAt(member);
        List<Member> matchingMemberList = memberRepository.findAllById(matchingMembers.stream()
                .map(MatchedInfo::getMatchedMemberId)
                .collect(Collectors.toList()));

        log.info("추천 회원의 책 리스트 가져오는 쿼리 ⬇️⬇️⬇️");
        Map<Long, MemberBook> memberBookMap = memberBookRepository.findAllById(matchingMembers.stream()
                        .map(MatchedInfo::getMatchedMemberBookId)
                        .collect(Collectors.toList()))
                .stream()
                .collect(Collectors.toMap(MemberBook::getId, book -> book));

        log.info("앱 사용자의 책 저자 리스트를 가져오는 쿼리 ⬇️⬇️⬇️");
        Map<Long, List<String>> memberBookAuthorsMap = memberBooks.stream()
                .collect(Collectors.toMap(
                        MemberBook::getId,
                        book -> book.getBook().getAuthors())
                );

        log.info("추천 회원의 책 저자 리스트를 가져오는 쿼리 ⬇️⬇️⬇️");
        Map<Long, List<String>> matchingMemberBookAuthorsMap = memberBookMap.values().stream()
                .collect(Collectors.toMap(
                        MemberBook::getId,
                        book -> book.getBook().getAuthors())
                );

        return matchingMembers.stream().map(matchedInfo -> {
            Member matchingMember = matchingMemberList.stream()
                    .filter(m -> m.getId().equals(matchedInfo.getMatchedMemberId()))
                    .findFirst()
                    .orElseThrow(() -> new BaseException(MemberMatchingExceptionType.MATCHING_MEMBER_DOESNT_EXIST));
            MemberBook matchingMemberBook = memberBookMap.get(matchedInfo.getMatchedMemberBookId());

            if (isSameSchool(member, matchingMember) && isSameBook(memberBooks, matchingMemberBook)) {
                matchedInfo.accumulateSimilarityWeight(SAME_SCHOOL_SAME_BOOK);
            } else if (!isSameSchool(member, matchingMember) && isSameBook(memberBooks, matchingMemberBook)) {
                matchedInfo.accumulateSimilarityWeight(OTHER_SCHOOL_SAME_BOOK);
            } else if (isSameSchool(member, matchingMember) && isSameAuthor(memberBookAuthorsMap, matchingMemberBookAuthorsMap, matchingMemberBook)) {
                matchedInfo.accumulateSimilarityWeight(SAME_SCHOOL_SAME_AUTHOR);
            } else if (!isSameSchool(member, matchingMember) && isSameAuthor(memberBookAuthorsMap, matchingMemberBookAuthorsMap, matchingMemberBook)) {
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

            return matchedInfo;
        }).collect(Collectors.toList());
    }

    private boolean isSameBook(List<MemberBook> memberBooks, MemberBook matchingMemberBook) {
        if (memberBooks == null || matchingMemberBook == null || matchingMemberBook.getBook() == null) {
            return false;
        }
        return memberBooks.stream()
                .filter(book -> book.getBook() != null)
                .anyMatch(book -> book.getBook().equals(matchingMemberBook.getBook()));
    }

    private boolean isSameAuthor(Map<Long, List<String>> memberBookAuthorsMap, Map<Long, List<String>> matchingMemberBookAuthorsMap, MemberBook matchingMemberBook) {
        if (matchingMemberBook == null || matchingMemberBook.getBook() == null) {
            return false;
        }

        // 매칭 멤버의 책 ID로 저자 리스트를 가져옴
        List<String> matchingAuthors = matchingMemberBookAuthorsMap.get(matchingMemberBook.getId());

        // 회원의 책들의 저자 리스트와 매칭 멤버의 저자 리스트를 비교
        return memberBookAuthorsMap.values().stream()
                .filter(authors -> authors != null && !authors.isEmpty())
                .anyMatch(authors -> matchingAuthors != null && !matchingAuthors.isEmpty() &&
                        authors.get(0).equals(matchingAuthors.get(0)));
    }

    private boolean isSameSchool(Member member, Member matchingMember) {
        if (member == null || matchingMember == null || member.getSchool() == null || matchingMember.getSchool() == null) {
            return false;
        }
        return member.getSchool().equals(matchingMember.getSchool());
    }

    private boolean isSameSmoking(Member member, Member matchingMember) {
        if (member == null || matchingMember == null || member.getMemberStyle() == null || matchingMember.getMemberStyle() == null) {
            return false;
        }
        return member.getMemberStyle().getSmokeType() == matchingMember.getMemberStyle().getSmokeType();
    }
}
