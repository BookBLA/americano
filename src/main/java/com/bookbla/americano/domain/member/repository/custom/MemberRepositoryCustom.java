package com.bookbla.americano.domain.member.repository.custom;

import com.bookbla.americano.domain.member.controller.dto.request.MemberBookProfileRequestDto;
import com.bookbla.americano.domain.member.controller.dto.response.BookProfileResponse;
import com.bookbla.americano.domain.member.service.dto.MemberRecommendationDto;
import com.bookbla.americano.domain.postcard.repository.entity.Postcard;

import java.util.List;
import java.util.Map;

public interface MemberRepositoryCustom {

    List<BookProfileResponse> getAllMembers(Long memberId, MemberBookProfileRequestDto requestDto);

    List<Map<Long, Long>> getRecommendationMemberIdsAndBookIds(MemberRecommendationDto memberRecommendationDto, List<Postcard> postcards);
}
