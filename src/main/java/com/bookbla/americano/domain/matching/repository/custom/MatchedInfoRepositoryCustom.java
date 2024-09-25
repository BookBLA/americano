package com.bookbla.americano.domain.matching.repository.custom;

import com.bookbla.americano.domain.matching.repository.entity.MatchedInfo;
import com.bookbla.americano.domain.matching.repository.entity.MemberMatching;

import java.util.List;

public interface MatchedInfoRepositoryCustom {

    List<MatchedInfo> findAllByMemberMatchingId(Long memberMatchingId);

    List<MatchedInfo> getAllByDesc(Long memberMatchingId);

    void saveAllRecommendedMembers(List<MatchedInfo> recommendedMembers);

    void updateAllRecommendedMembers(MemberMatching memberMatching, List<MatchedInfo> recommendedMembers);
}
