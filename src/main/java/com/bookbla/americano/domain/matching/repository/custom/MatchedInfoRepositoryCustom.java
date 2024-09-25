package com.bookbla.americano.domain.matching.repository.custom;

import com.bookbla.americano.domain.matching.repository.entity.MatchedInfo;

import java.util.List;

public interface MatchedInfoRepositoryCustom {

    List<MatchedInfo> findAllByMemberMatchingId(Long memberMatchingId);

    List<MatchedInfo> getAllByDesc(Long memberMatchingId);

    long deleteByMemberMatchingId(Long memberMatchingId);
}
