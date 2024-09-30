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
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * 주석은 추후 삭제 예정
 */
@Service
@Transactional
@RequiredArgsConstructor
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

        List<MatchedInfo> matchedMemberList = matchedInfoRepository.findAllByMemberMatchingId(memberMatching.getId());

        if (!matchedMemberList.isEmpty()) {
            /** 조건 전부 쿼리에서 조건 걸고 땡겨옴
             *   회원 탈퇴 (O)
             *   매칭 비활성화 (O)
             *   신고 (O)
             *   차단 (O)
             *   엽서 (X) -> 엽서 거절 부분 못함 그외 ok
             **/
            matchedInfoRepository.deleteByMemberMatchingId(memberMatching.getId());

            return buildMemberIntroResponse(getMostPriorityMatched(matchedMemberList));
        }

        MemberRecommendationDto memberRecommendationDto = MemberRecommendationDto.from(member);

        // 추천회원 id와 추천회원의 책 id 추출
        List<MatchedInfo> recommendedMembers = memberMatchingRepository
                .getMatchingMembers(memberRecommendationDto);

        // 차단한 회원 필터링
        recommendedMembers = memberMatchingFilter.memberBlockedFiltering(member.getId(), recommendedMembers);

        // 학생증 인증 필터링
        recommendedMembers = memberMatchingFilter.memberVerifyFiltering(recommendedMembers);

        // "거절 + 14일 < 오늘" 필터링
        recommendedMembers = memberMatchingFilter.memberRefusedAtFiltering(member.getId(), recommendedMembers);

        // 우선순위 알고리즘 적용
        recommendedMembers = memberMatchingAlgorithmFilter.memberMatchingAlgorithmFiltering(member, recommendedMembers);

        updateAllRecommendedMembers(memberMatching, recommendedMembers);

        MatchedInfo matchedInfo = getMostPriorityMatched(matchedInfoRepository.getAllByDesc(memberMatching.getId()));

        return buildMemberIntroResponse(matchedInfo);
    }

    public MemberIntroResponse refreshMemberMatching(Long memberId, Long refreshMemberId, Long refreshMemberBookId) {
        matchIgnoredRepository.findByMemberIdAndIgnoredMemberIdAndIgnoredMemberBookId(memberId, refreshMemberId, refreshMemberBookId)
                .orElseGet(() -> matchIgnoredRepository.save(MatchIgnoredInfo.from(memberId, refreshMemberId, refreshMemberBookId)));

        return buildMemberIntroResponse(popMostPriorityMatched(memberId, refreshMemberId, refreshMemberBookId));
    }

    public void rejectMemberMatching(Long memberId, Long rejectedMemberId) {
        matchExcludedRepository.findByMemberIdAndExcludedMemberId(memberId, rejectedMemberId)
                .orElseGet(() -> matchExcludedRepository.save(MatchExcludedInfo.of(memberId, rejectedMemberId)));
    }

    private MemberIntroResponse buildMemberIntroResponse(MatchedInfo matchedInfo) {
        if (matchedInfo == null) return MemberIntroResponse.from();

        MemberMatching memberMatching = memberMatchingRepository.getByIdOrThrow(matchedInfo.getMemberMatching().getId());
        Member matchedMember = memberRepository.getByIdOrThrow(matchedInfo.getMatchedMemberId());
        MemberBook matchedMemberBook = memberBookRepository.getByIdOrThrow(matchedInfo.getMatchedMemberBookId());

        if (matchedMember == null || matchedMemberBook == null) {
            throw new BaseException(MemberMatchingExceptionType.MATCHING_MEMBER_DOESNT_EXIST);
        }

        memberMatching.updateCurrentMatchedInfo(matchedMember.getId(), matchedMemberBook.getId());

        return MemberIntroResponse.from(matchedMember, matchedMemberBook);
    }

    private MatchedInfo getMostPriorityMatched(List<MatchedInfo> matchedMemberList) {
        if (matchedMemberList.isEmpty()) {
            return null;
//            throw new BaseException(MemberMatchingExceptionType.MATCHING_MEMBER_DOESNT_EXIST);
        }

        return matchedMemberList.get(0);
    }

    private MatchedInfo popMostPriorityMatched(Long memberId, Long refreshMemberId, Long refreshMemberBookId) {
        matchedInfoRepository.deleteByMemberIdAndMatchedMemberIdAndMatchedMemberBookId(memberId, refreshMemberId, refreshMemberBookId);

        MemberMatching memberMatching = memberMatchingRepository.findByMemberId(memberId)
                .orElseThrow(() -> new BaseException(MemberMatchingExceptionType.MATCHING_MEMBER_DOESNT_EXIST));

        List<MatchedInfo> recommendedMembers = matchedInfoRepository.findAllByMemberMatchingId(memberMatching.getId());

        if (recommendedMembers.isEmpty()) {
            throw new BaseException(MemberMatchingExceptionType.MATCHING_MEMBER_DOESNT_EXIST);
        }

        return recommendedMembers.get(0);
    }

    private void updateAllRecommendedMembers(MemberMatching memberMatching, List<MatchedInfo> recommendedMembers) {
        String sql = "UPDATE matched_info SET member_matching_id = ? WHERE member_id = ? AND matched_member_id = ? AND matched_member_book_id = ?";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                MatchedInfo matchedInfo = recommendedMembers.get(i);
                ps.setDouble(1, memberMatching.getId());
                ps.setLong(2, matchedInfo.getMemberId());
                ps.setLong(3, matchedInfo.getMatchedMemberId());
                ps.setLong(4, matchedInfo.getMatchedMemberBookId());
            }

            @Override
            public int getBatchSize() {
                return recommendedMembers.size(); // 전체 리스트 크기
            }
        });
    }
}