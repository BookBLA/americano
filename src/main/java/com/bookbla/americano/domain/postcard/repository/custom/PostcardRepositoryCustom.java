package com.bookbla.americano.domain.postcard.repository.custom;

import com.bookbla.americano.domain.matching.repository.entity.MatchedInfo;
import com.bookbla.americano.domain.matching.service.dto.MemberRecommendationDto;
import com.bookbla.americano.domain.postcard.repository.entity.Postcard;
import com.bookbla.americano.domain.postcard.service.dto.response.PostcardFromResponse;
import com.bookbla.americano.domain.postcard.service.dto.response.PostcardToResponse;

import java.util.List;

public interface PostcardRepositoryCustom {

    List<PostcardFromResponse> getPostcardsFromMember(Long memberId);

    List<PostcardToResponse> getPostcardsToMember(Long memberId);

    List<Postcard> refuseExpiredPostcard();

    List<MatchedInfo> getReceiveIdsAndReceiveMemberBookIdsByRefusedAt(MemberRecommendationDto memberRecommendationDto);
}
