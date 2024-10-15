package com.bookbla.americano.domain.matching.repository;

import com.bookbla.americano.domain.matching.repository.custom.MatchedInfoRepositoryCustom;
import com.bookbla.americano.domain.matching.repository.entity.MatchedInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatchedInfoRepository extends JpaRepository<MatchedInfo, Long>, MatchedInfoRepositoryCustom {

    void deleteByMemberIdAndMatchedMemberIdAndMatchedMemberBookId(Long memberId, Long matchedMemberId, Long matchedMemberBookId);

    Optional<MatchedInfo> findByMemberIdAndMatchedMemberIdAndMatchedMemberBookId(Long memberId, Long matchedMemberId, Long matchedMemberBookId);

    void deleteByMemberIdAndMatchedMemberId(Long memberId, Long matchedMemberId);
}
