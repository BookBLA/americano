package com.bookbla.americano.domain.matching.service;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.matching.controller.dto.response.MemberIntroResponse;
import com.bookbla.americano.domain.matching.exception.MemberMatchingExceptionType;
import com.bookbla.americano.domain.matching.repository.MatchExcludedRepository;
import com.bookbla.americano.domain.matching.repository.MatchIgnoredRepository;
import com.bookbla.americano.domain.matching.repository.MatchedInfoRepository;
import com.bookbla.americano.domain.matching.repository.MemberMatchingRepository;
import com.bookbla.americano.domain.matching.repository.entity.MatchExcludedInfo;
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
    private final MatchExcludedRepository matchExcludedRepository;
    private final MatchIgnoredRepository matchIgnoredRepository;

    private final JdbcTemplate jdbcTemplate;

    public MemberIntroResponse getRecommendationMember(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);

        MemberMatching memberMatching = memberMatchingRepository.findByMemberId(memberId)
                .orElseGet(() -> memberMatchingRepository.save(MemberMatching.of(member)));

        member.updateLastUsedAt();

        if (memberMatching.hasCurrentMatchedInfo()) {
            MatchedInfo matchedInfo = getMatchedInfo(memberId, memberMatching);

            memberMatching.updateInvitationCard(false);

            return buildMemberIntroResponse(matchedInfo, memberMatching);
        }

        MemberRecommendationDto memberRecommendationDto = MemberRecommendationDto.from(member, memberMatching);

        List<MatchedInfo> recommendedMembers = memberMatchingRepository
                .getMatchingMembers(memberRecommendationDto);
        log.info("최소 조건으로 추출한 추천 회원 수: {}", recommendedMembers.size());

        recommendedMembers = memberMatchingFilter.memberBlockedFiltering(member.getId(), recommendedMembers);
        log.info("차단한 회원 필터링 후 추천 회원 수: {}", recommendedMembers.size());

        recommendedMembers = memberMatchingFilter.memberRefusedAtFiltering(member.getId(), recommendedMembers);
        log.info("엽서 거절 필터링 후 추천 회원 수: {}", recommendedMembers.size());

        log.info("알고리즘 가중치 적용 쿼리 ⬇️⬇️⬇️");
        recommendedMembers = memberMatchingAlgorithmFilter.memberMatchingAlgorithmFiltering(member, recommendedMembers);

        log.info("필터링된 회원 저장 쿼리 ⬇️⬇️⬇️");
        saveAllRecommendedMembers(recommendedMembers);

        MatchedInfo matchedInfo = getMostPriorityMatched(matchedInfoRepository.getAllByDesc(memberMatching.getId()));

        MemberIntroResponse memberIntroResponse = buildMemberIntroResponse(matchedInfo, memberMatching);

        updateCurrentMatchedInfo(memberMatching, memberIntroResponse.getMemberId(), memberIntroResponse.getMemberBookId());

        return memberIntroResponse;
    }

    private MatchedInfo getMatchedInfo(Long memberId, MemberMatching memberMatching) {
        return matchedInfoRepository.findByMemberIdAndMatchedMemberIdAndMatchedMemberBookId(memberId,
                        memberMatching.getCurrentMatchedMemberId(), memberMatching.getCurrentMatchedMemberBookId())
                .orElseThrow(() -> new BaseException(MemberMatchingExceptionType.MATCHING_MEMBER_DOESNT_EXIST));
    }

    public MemberIntroResponse refreshMemberMatching(Long memberId) {
        MemberMatching memberMatching = memberMatchingRepository.findByMemberId(memberId)
                .orElseThrow(() -> new BaseException(MemberMatchingExceptionType.NOT_FOUND_MATCHING));

        Long refreshMemberId = memberMatching.getCurrentMatchedMemberId();
        Long refreshMemberBookId = memberMatching.getCurrentMatchedMemberBookId();

        matchIgnoredRepository.findByMemberIdAndIgnoredMemberIdAndIgnoredMemberBookId(memberId, refreshMemberId, refreshMemberBookId)
                .orElseGet(() -> matchIgnoredRepository.save(MatchIgnoredInfo.from(memberId, refreshMemberId, refreshMemberBookId)));

        MatchedInfo matchedInfo = popMostPriorityMatched(memberMatching.getId(), memberId, refreshMemberId, refreshMemberBookId);

        updateCurrentMatchedInfo(memberMatching, matchedInfo.getMatchedMemberId(), matchedInfo.getMatchedMemberBookId());

        memberMatching.updateInvitationCard(false);
        return buildMemberIntroResponse(matchedInfo, memberMatching);
    }

    public void rejectMemberMatching(Long memberId, Long rejectedMemberId) {
        matchExcludedRepository.findByMemberIdAndExcludedMemberId(memberId, rejectedMemberId)
                .orElseGet(() -> matchExcludedRepository.save(MatchExcludedInfo.of(memberId, rejectedMemberId)));
    }

    private MemberIntroResponse buildMemberIntroResponse(MatchedInfo matchedInfo, MemberMatching memberMatching) {
        if (matchedInfo == null) return MemberIntroResponse.empty();

        Member matchedMember = memberRepository.getByIdOrThrow(matchedInfo.getMatchedMemberId());
        MemberBook matchedMemberBook = memberBookRepository.getByIdOrThrow(matchedInfo.getMatchedMemberBookId());

        if (matchedMember == null || matchedMemberBook == null) {
            throw new BaseException(MemberMatchingExceptionType.MATCHING_MEMBER_DOESNT_EXIST);
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

    private void saveAllRecommendedMembers(List<MatchedInfo> recommendedMembers) {
        String sql = "INSERT INTO matched_info (member_id, matched_member_id, matched_member_book_id, member_matching_id, similarity_weight) " +
                "VALUES (?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "similarity_weight = VALUES(similarity_weight)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                MatchedInfo matchedInfo = recommendedMembers.get(i);
                ps.setLong(1, matchedInfo.getMemberId());
                ps.setLong(2, matchedInfo.getMatchedMemberId());
                ps.setLong(3, matchedInfo.getMatchedMemberBookId());
                ps.setLong(4, matchedInfo.getMemberMatching().getId());
                ps.setDouble(5, matchedInfo.getSimilarityWeight());
            }

            @Override
            public int getBatchSize() {
                return recommendedMembers.size(); // 전체 리스트 크기
            }
        });
    }

    private void updateCurrentMatchedInfo(MemberMatching memberMatching, Long currentMatchedMemberId, Long currentMatchedMemberBookId) {
        memberMatching.updateCurrentMatchedInfo(currentMatchedMemberId, currentMatchedMemberBookId);
        memberMatchingRepository.save(memberMatching);
    }
}