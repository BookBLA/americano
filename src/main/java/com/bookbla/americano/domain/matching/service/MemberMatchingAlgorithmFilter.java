package com.bookbla.americano.domain.matching.service;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.matching.exception.MemberMatchingExceptionType;
import com.bookbla.americano.domain.member.repository.MemberBookRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    private final Map<Long, Double> priorityMap = new HashMap<>();

    public List<Map<Long, Long>> memberMatchingAlgorithmFiltering(Member member, List<Map<Long, Long>> matchingMembers) {

        List<Map<Long, Double>> weightedMatchingMembers;

        for (Map<Long, Long> matchingMember : matchingMembers) {
            for (Long memberId : matchingMember.keySet()) {
                Member matchingMemberEntity = memberRepository.getByIdOrThrow(memberId);

                double weight = 0.0;

                // 같은 학교 같은 책
                if (member.getSchool().equals(matchingMemberEntity.getSchool()) && isSameBook(member, matchingMemberEntity)) {
                    weight = 1.0;
                }
                // 다른 학교 같은 책
                else if (!member.getSchool().equals(matchingMemberEntity.getSchool()) && isSameBook(member, matchingMemberEntity)) {
                    weight = 0.9;
                }
                // 같은 학교 같은 작가
                else if (member.getSchool().equals(matchingMemberEntity.getSchool()) && isSameAuthor(member, matchingMemberEntity)) {
                    weight = 0.8;
                }
                // 다른 학교 같은 작가
                else if (!member.getSchool().equals(matchingMemberEntity.getSchool()) && isSameAuthor(member, matchingMemberEntity)) {
                    weight = 0.7;
                }
                // 같은 학교 흡연여부 동일
                else if (member.getSchool().equals(matchingMemberEntity.getSchool()) && isSameSmoking(member, matchingMemberEntity)) {
                    weight = 0.6;
                }
                // 다른 학교 흡연여부 동일
                else if (!member.getSchool().equals(matchingMemberEntity.getSchool()) && isSameSmoking(member, matchingMemberEntity)) {
                    weight = 0.5;
                }

                priorityMap.merge(memberId, weight, Double::sum);
            }
        }

        weightedMatchingMembers = priorityMap.entrySet().stream()
                .sorted((entry1, entry2) -> Double.compare(entry2.getValue(), entry1.getValue()))  // 가중치 큰 순으로 정렬
                .map(entry -> {
                    Map<Long, Double> weightedMember = new HashMap<>();
                    weightedMember.put(entry.getKey(), entry.getValue());
                    return weightedMember;
                })
                .collect(Collectors.toList());

        // 가중치가 높은 순으로 정렬된 매칭 멤버들을 반환
        return weightedMatchingMembers.stream()
                .map(weightedMember -> {
                    Long memberId = weightedMember.keySet().iterator().next();
                    Long memberBookId = matchingMembers.stream()
                            .filter(matchingMember -> matchingMember.containsKey(memberId))
                            .map(matchingMember -> matchingMember.get(memberId))
                            .findFirst()
                            .orElseThrow(() -> new BaseException(MemberMatchingExceptionType.NOT_FOUND_MEMBER_BOOK));
                    Map<Long, Long> matchingMember = new HashMap<>();
                    matchingMember.put(memberId, memberBookId);
                    return matchingMember;
                })
                .collect(Collectors.toList());
    }

    private boolean isSameBook(Member member, Member matchingMember) {
        List<MemberBook> memberBooks = memberBookRepository.findByMemberOrderByCreatedAt(member);
        List<MemberBook> matchingMemberBooks = memberBookRepository.findByMemberOrderByCreatedAt(matchingMember);

        for (MemberBook memberBook : memberBooks) {
            for (MemberBook matchingMemberBook : matchingMemberBooks) {
                if (memberBook.getBook().equals(matchingMemberBook.getBook())) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isSameAuthor(Member member, Member matchingMember) {
        List<MemberBook> memberBooks = memberBookRepository.findByMemberOrderByCreatedAt(member);
        List<MemberBook> matchingMemberBooks = memberBookRepository.findByMemberOrderByCreatedAt(matchingMember);

        for (MemberBook memberBook : memberBooks) {
            for (MemberBook matchingMemberBook : matchingMemberBooks) {
                // 대표 저자로 확인
                if (memberBook.getBook().getAuthors().get(0).equals(matchingMemberBook.getBook().getAuthors().get(0))) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isSameSmoking(Member member, Member matchingMember) {
        return member.getMemberStyle().getSmokeType() == matchingMember.getMemberStyle().getSmokeType();
    }
}
