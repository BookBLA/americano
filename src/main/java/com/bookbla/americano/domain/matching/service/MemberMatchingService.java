package com.bookbla.americano.domain.matching.service;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.matching.controller.dto.response.MemberIntroResponse;
import com.bookbla.americano.domain.matching.exception.MemberMatchingExceptionType;
import com.bookbla.americano.domain.matching.repository.MatchIgnoredRepository;
import com.bookbla.americano.domain.matching.repository.MatchedInfoRepository;
import com.bookbla.americano.domain.matching.repository.MemberMatchingRepository;
import com.bookbla.americano.domain.matching.repository.entity.MatchIgnoredInfo;
import com.bookbla.americano.domain.matching.repository.entity.MatchedInfo;
import com.bookbla.americano.domain.matching.repository.entity.MemberMatching;
import com.bookbla.americano.domain.matching.service.dto.MemberRecommendationDto;
import com.bookbla.americano.domain.member.repository.MemberBookRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberMatchingService {

    private final MemberRepository memberRepository;
    private final MemberBookRepository memberBookRepository;
    private final MemberMatchingRepository memberMatchingRepository;
    private final MemberMatchingFilter memberMatchingFilter;
    private final MemberMatchingAlgorithmFilter memberMatchingAlgorithmFilter;
    private final MatchedInfoRepository matchedInfoRepository;
    private final MatchIgnoredRepository matchIgnoredRepository;

    private final JdbcTemplate jdbcTemplate;

    public MemberIntroResponse getHomeMatch(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        member.updateLastUsedAt();

        MemberMatching memberMatching = memberMatchingRepository.findByMemberId(memberId)
                .orElseGet(() -> memberMatchingRepository.save(MemberMatching.of(member)));

        // value, value, false -> 현재 매칭 정보 보여주기
        if (memberMatching.hasCurrentMatchedInfo()) {
            MatchedInfo matchedInfo = getMatchedInfo(memberId, memberMatching);

            return buildMemberIntroResponseWithMatchedInfo(matchedInfo, memberMatching);
        }

        // null null true + matched_info 존재
        List<MatchedInfo> matchedInfoList = matchedInfoRepository.getAllByDesc(memberMatching.getId());
        if (!matchedInfoList.isEmpty()) {
            MatchedInfo matchedInfo = getMostPriorityMatched(matchedInfoList);
            if (matchedInfo == null) {
                return MemberIntroResponse.empty();
            }
            return buildMemberIntroResponseWithMatchedInfo(matchedInfo, memberMatching);
        }

        // null null true + matched_info 없음 & null null false + matched_info 없음
        saveRecommendationMatches(memberId); // matched_info 저장

        MatchedInfo matchedInfo = getMostPriorityMatched(matchedInfoRepository.getAllByDesc(memberMatching.getId()));
        // null null true + matched_info 없음 & null null false + matched_info 없음
        if (matchedInfo == null) {
            if (memberMatching.getIsInvitationCard()) { // null null true + matched_info 없음
                return MemberIntroResponse.showInvitationCard();
            } else {  // null null false + matched_info 없음
                return MemberIntroResponse.empty();
            }
        }

        // null null true + matched_info 존재 & null null false + matched_info 존재
        if (memberMatching.mealInvitationCard()) { // null null false + matched_info 존재
            memberMatching.updateInvitationCard(true);
            return MemberIntroResponse.showInvitationCard();
        }
        // null null true + matched_info 존재
        return buildMemberIntroResponseWithMatchedInfo(matchedInfo, memberMatching);
    }

    private MatchedInfo getMatchedInfo(Long memberId, MemberMatching memberMatching) {
        return matchedInfoRepository.findByMemberIdAndMatchedMemberIdAndMatchedMemberBookId(memberId, memberMatching.getCurrentMatchedMemberId(), memberMatching.getCurrentMatchedMemberBookId())
                .orElseThrow(() -> new BaseException(MemberMatchingExceptionType.MATCHING_INFO_DOESNT_EXIST));
    }

    public MemberIntroResponse refreshMemberMatching(Long memberId) {
        MemberMatching memberMatching = memberMatchingRepository.findByMemberId(memberId)
                .orElseThrow(() -> new BaseException(MemberMatchingExceptionType.MEMBER_MATCHING_NOT_FOUND));

        // null null false + matched_info 없음
        if (memberMatching.mealInvitationCard()) {
            return getHomeMatch(memberId);
        }
        // null null true + matched_info 없음 & null null true + matched_info 존재
        if (!memberMatching.hasCurrentMatchedInfo() && memberMatching.getIsInvitationCard()) {
            MemberIntroResponse memberIntroResponse = getHomeMatch(memberId);

            if (memberIntroResponse.equalsShowInvitationCard(MemberIntroResponse.showInvitationCard())) { // null null true + matched_info 없음
                memberMatching.updateInvitationCard(false);
                return MemberIntroResponse.empty();
            }
            // null null true + matched_info 존재
            updateCurrentMatchedInfo(memberMatching, memberIntroResponse.getMemberId(), memberIntroResponse.getMemberBookId());
            memberMatching.updateInvitationCard(false);
            return buildMemberIntroResponseWithMemberIntroResponse(memberIntroResponse, memberMatching);
        }

        // value value false + matched_info 존재 & value value false + matched_info 없음
        Long refreshMemberId = memberMatching.getCurrentMatchedMemberId();
        Long refreshMemberBookId = memberMatching.getCurrentMatchedMemberBookId();

        matchIgnoredRepository.findByMemberIdAndIgnoredMemberIdAndIgnoredMemberBookId(memberId, refreshMemberId, refreshMemberBookId)
                .ifPresentOrElse(ignoredInfo -> {
                    ignoredInfo.updateIgnoredAt();
                }, () -> {
                    matchIgnoredRepository.save(MatchIgnoredInfo.from(memberId, refreshMemberId, refreshMemberBookId));
                });

        // 다음 매칭정보 가져옴
        MatchedInfo matchedInfo = popMostPriorityMatched(memberMatching.getId(), memberId, refreshMemberId, refreshMemberBookId);

        if (matchedInfo == null) { // value value false + matched_info 없음 -> 기획자 식사 카드
            updateCurrentMatchedInfo(memberMatching, null, null);
            memberMatching.updateInvitationCard(false);
            return MemberIntroResponse.empty();
        }

        // value value false + matched_info 존재
        updateCurrentMatchedInfo(memberMatching, matchedInfo.getMatchedMemberId(), matchedInfo.getMatchedMemberBookId());
        memberMatching.updateInvitationCard(false);

        return buildMemberIntroResponseWithMatchedInfo(matchedInfo, memberMatching);
    }

    private MemberIntroResponse buildMemberIntroResponseWithMatchedInfo(MatchedInfo matchedInfo, MemberMatching memberMatching) {
        if (matchedInfo == null) return MemberIntroResponse.empty();

        Member matchedMember = memberRepository.getByIdOrThrow(matchedInfo.getMatchedMemberId());
        MemberBook matchedMemberBook = memberBookRepository.getByIdOrThrow(matchedInfo.getMatchedMemberBookId());

        if (matchedMember == null || matchedMemberBook == null) {
            throw new BaseException(MemberMatchingExceptionType.VALID_MATCHING_INFO);
        }

        return MemberIntroResponse.from(matchedMember, matchedMemberBook, memberMatching);
    }

    private MemberIntroResponse buildMemberIntroResponseWithMemberIntroResponse(MemberIntroResponse introResponse, MemberMatching memberMatching) {
        if (introResponse == null) return MemberIntroResponse.empty();

        Member matchedMember = memberRepository.getByIdOrThrow(introResponse.getMemberId());
        MemberBook matchedMemberBook = memberBookRepository.getByIdOrThrow(introResponse.getMemberBookId());

        if (matchedMember == null || matchedMemberBook == null) {
            throw new BaseException(MemberMatchingExceptionType.VALID_MATCHING_INFO);
        }

        return MemberIntroResponse.from(matchedMember, matchedMemberBook, memberMatching);
    }

    private MatchedInfo getMostPriorityMatched(List<MatchedInfo> matchedMemberList) {
        if (matchedMemberList.isEmpty()) return null;

        return matchedMemberList.get(0);
    }

    private MatchedInfo popMostPriorityMatched(Long memberMatchingId, Long memberId, Long refreshMemberId, Long refreshMemberBookId) {
        matchedInfoRepository.deleteByMemberIdAndMatchedMemberIdAndMatchedMemberBookId(memberId, refreshMemberId, refreshMemberBookId);

        return getMostPriorityMatched(matchedInfoRepository.findAllByMemberMatchingId(memberMatchingId));
    }

    private void saveRecommendationMatches(Long memberId) {
        log.info("알고리즘 매칭 시작 ⬇️⬇️⬇️");
        Member member = memberRepository.getByIdOrThrow(memberId);

        MemberMatching memberMatching = memberMatchingRepository.findByMemberId(memberId).orElseThrow(() -> new BaseException(MemberMatchingExceptionType.MEMBER_MATCHING_NOT_FOUND));

        MemberRecommendationDto memberRecommendationDto = MemberRecommendationDto.from(member, memberMatching);

        List<Long> recommendedMemberIds = memberMatchingRepository.getMinimumConstraintMemberIds(memberRecommendationDto);
        log.info("최소 조건(회원 상태)으로 추출한 추천 회원 ID 수: {}", recommendedMemberIds.size());

        recommendedMemberIds = memberMatchingFilter.memberExcludedFiltering(member.getId(), recommendedMemberIds);
        log.info("제외된 회원 필터링 한 추천 회원 ID 수: {}", recommendedMemberIds.size());

        List<MatchedInfo> recommendedMatches = memberMatchingFilter.memberRefusedAtFiltering(recommendedMemberIds, memberRecommendationDto);
        log.info("엽서 거절 필터링 후 추천 매칭 정보 수: {}", recommendedMatches.size());

        recommendedMatches = memberMatchingFilter.memberIgnoredFiltering(recommendedMatches, memberRecommendationDto);
        log.info("MatchIgnoredInfo 필터링 후 ❗️최종 매칭 추천 수: {}", recommendedMatches.size());

        log.info("알고리즘 가중치 적용 쿼리 ⬇️⬇️⬇️");
        recommendedMatches = memberMatchingAlgorithmFilter.memberMatchingAlgorithmFiltering(member, recommendedMatches);

        log.info("필터링된 매칭 정보 저장 쿼리 ⬇️⬇️⬇️");
        saveAllRecommendedMatches(recommendedMatches);
    }

    private void saveAllRecommendedMatches(List<MatchedInfo> recommendedMatches) {
        String sql = "INSERT INTO matched_info (member_id, matched_member_id, matched_member_book_id, member_matching_id, similarity_weight) " + "VALUES (?, ?, ?, ?, ?) " + "ON DUPLICATE KEY UPDATE " + "similarity_weight = VALUES(similarity_weight)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                MatchedInfo matchedInfo = recommendedMatches.get(i);
                ps.setLong(1, matchedInfo.getMemberId());
                ps.setLong(2, matchedInfo.getMatchedMemberId());
                ps.setLong(3, matchedInfo.getMatchedMemberBookId());
                ps.setLong(4, matchedInfo.getMemberMatching().getId());
                ps.setDouble(5, matchedInfo.getSimilarityWeight());
            }

            @Override
            public int getBatchSize() {
                return recommendedMatches.size(); // 전체 리스트 크기
            }
        });
    }

    private void updateCurrentMatchedInfo(MemberMatching memberMatching, Long currentMatchedMemberId, Long currentMatchedMemberBookId) {
        memberMatching.updateCurrentMatchedInfo(currentMatchedMemberId, currentMatchedMemberBookId);
        memberMatchingRepository.save(memberMatching);
    }
}