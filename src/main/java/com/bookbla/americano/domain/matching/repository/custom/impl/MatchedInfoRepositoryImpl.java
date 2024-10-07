package com.bookbla.americano.domain.matching.repository.custom.impl;

import com.bookbla.americano.domain.matching.repository.custom.MatchedInfoRepositoryCustom;
import com.bookbla.americano.domain.matching.repository.entity.MatchedInfo;
import com.bookbla.americano.domain.matching.service.dto.MemberRecommendationDto;
import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.bookbla.americano.domain.postcard.enums.PostcardStatus;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.bookbla.americano.domain.matching.repository.entity.QMatchIgnoredInfo.matchIgnoredInfo;
import static com.bookbla.americano.domain.matching.repository.entity.QMatchedInfo.matchedInfo;
import static com.bookbla.americano.domain.member.repository.entity.QMember.member;
import static com.bookbla.americano.domain.member.repository.entity.QMemberBook.memberBook;
import static com.bookbla.americano.domain.postcard.repository.entity.QPostcard.postcard;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MatchedInfoRepositoryImpl implements MatchedInfoRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MatchedInfo> findAllByMemberMatchingId(Long memberMatchingId) {
        return queryFactory
                .selectFrom(matchedInfo)
                .innerJoin(member).on(matchedInfo.matchedMemberId.eq(member.id))
//                .leftJoin(matchExcludedInfo).on(matchedInfo.matchedMemberId.eq(matchExcludedInfo.excludedMemberId))
//                .leftJoin(matchIgnoredInfo).on(matchedInfo.matchedMemberId.eq(matchIgnoredInfo.ignoredMemberId))
                .where(
                        matchedInfo.memberMatching.id.eq(memberMatchingId),
//                        matchExcludedInfo.excludedMemberId.ne(matchedInfo.memberId)
//                                .or(matchIgnoredInfo.ignoredMemberId.isNull()),
//                        matchIgnoredInfo.ignoredMemberId.ne(matchedInfo.memberId),
                        member.memberStatus.ne(MemberStatus.DELETED),
                        member.memberStatus.ne(MemberStatus.MATCHING_DISABLED),
                        member.memberStatus.ne(MemberStatus.REPORTED))
                .orderBy(matchedInfo.similarityWeight.desc())
                .fetch();
    }

    @Override
    public List<MatchedInfo> getAllByDesc(Long memberMatchingId) {
        return queryFactory
                .selectFrom(matchedInfo)
                .where(matchedInfo.memberMatching.id.eq(memberMatchingId))
                .orderBy(matchedInfo.similarityWeight.desc())
                .fetch();
    }

    @Override
    public List<MatchedInfo> getAllMatches(List<Long> matchingMemberIds, MemberRecommendationDto recommendationDto) {
        return queryFactory
                .select(member.id, memberBook.id)
                .from(member)
                .innerJoin(memberBook).on(member.id.eq(memberBook.member.id))
                .where(member.id.in(matchingMemberIds),
                        memberBook.isDeleted.isFalse())
                .fetch()
                .stream()
                .map(tuple -> MatchedInfo.from(recommendationDto.getMemberId(), tuple.get(member.id), tuple.get(memberBook.id), recommendationDto.getMemberMatching()))
                .collect(Collectors.toList());
    }

    @Override
    public List<MatchedInfo> getFinalFilteredMatches(List<Long> matchingMemberIds, MemberRecommendationDto recommendationDto) {

        LocalDateTime twoWeeksAgo = LocalDateTime.now().minusWeeks(2);

        // 모든 매칭 정보 ( member_id + member_book_id )
        List<MatchedInfo> matches = queryFactory
                .select(member.id, memberBook.id)
                .from(member)
                .innerJoin(memberBook).on(member.id.eq(memberBook.member.id))
                .where(member.id.in(matchingMemberIds),
                        memberBook.isDeleted.isFalse())
                .fetch()
                .stream()
                .map(tuple -> MatchedInfo.from(recommendationDto.getMemberId(), tuple.get(member.id), tuple.get(memberBook.id), recommendationDto.getMemberMatching()))
                .collect(Collectors.toList());
        log.info("모든 추천 매칭 정보 수: {}", matches.size());

        // 엽서 거절 정보
        List<MatchedInfo> filteredMatchesByPostcard = queryFactory
                .select(postcard.receiveMember.id, postcard.receiveMemberBook.id)
                .from(postcard)
                .where(postcard.sendMember.id.eq(recommendationDto.getMemberId()),
                        postcard.postcardStatus.eq(PostcardStatus.REFUSED),
                        postcard.postcardStatusRefusedAt.after(twoWeeksAgo))
                .fetch()
                .stream()
                .map(tuple -> MatchedInfo.from(recommendationDto.getMemberId(), tuple.get(member.id), tuple.get(memberBook.id), recommendationDto.getMemberMatching()))
                .collect(Collectors.toList());
        log.info("엽서 거절 필터링 추천 매칭 정보 수: {}", filteredMatchesByPostcard.size());

        // 모든 매칭 정보 - 엽서 거절
        List<MatchedInfo> filteredMatches = getFilteringMatches(matches, filteredMatchesByPostcard);

        // 무시된 정보
        List<MatchedInfo> filteredMatchesByIgnoredInfo = queryFactory
                .select(member.id, memberBook.id)
                .from(member)
                .innerJoin(memberBook).on(member.id.eq(memberBook.member.id))
                .leftJoin(matchIgnoredInfo).on(member.id.eq(matchIgnoredInfo.ignoredMemberId)
                        .and(memberBook.id.eq(matchIgnoredInfo.ignoredMemberBookId)))
                .where(matchIgnoredInfo.memberId.eq(recommendationDto.getMemberId()))
                .fetch()
                .stream()
                .map(tuple -> MatchedInfo.from(recommendationDto.getMemberId(), tuple.get(member.id), tuple.get(memberBook.id), recommendationDto.getMemberMatching()))
                .collect(Collectors.toList());
        log.info("matchIgnoredInfo 필터링 추천 매칭 정보 수: {}", filteredMatchesByIgnoredInfo.size());

        // 모든 매칭 정보 - 엽서 거절 필터링 - matchIgnoredInfo 필터링
        return getFilteringMatches(filteredMatches, filteredMatchesByIgnoredInfo);
    }

    @NotNull
    private static List<MatchedInfo> getFilteringMatches(List<MatchedInfo> matches, List<MatchedInfo> filteredMatches) {
        return matches.stream()
                .filter(match -> filteredMatches.stream()
                        .noneMatch(filteredMatch -> filteredMatch.getMatchedMemberId().equals(match.getMatchedMemberId())
                                                        && filteredMatch.getMatchedMemberBookId().equals(match.getMatchedMemberBookId())))
                .collect(Collectors.toList());
    }

    @Override
    public long deleteByMemberMatchingId(Long memberMatchingId) {
        return queryFactory
                .delete(matchedInfo)
                .where(
                        matchedInfo.memberMatching.id.eq(memberMatchingId),
                        matchedInfo.memberId.in(
                                JPAExpressions.select(member.id)
                                        .from(member)
                                        .where(member.memberStatus.eq(MemberStatus.DELETED)
                                                .or(member.memberStatus.eq(MemberStatus.MATCHING_DISABLED))
                                                .or(member.memberStatus.eq(MemberStatus.REPORTED)))
                        )
                )
                .execute();
    }
}
