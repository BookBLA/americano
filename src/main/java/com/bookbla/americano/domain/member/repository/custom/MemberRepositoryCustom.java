package com.bookbla.americano.domain.member.repository.custom;

import com.bookbla.americano.domain.member.controller.dto.request.MemberBookProfileRequestDto;
import com.bookbla.americano.domain.member.controller.dto.response.BookProfileResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberRecommendationResponse;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import com.bookbla.americano.domain.postcard.repository.entity.Postcard;

import java.util.List;

public interface MemberRepositoryCustom {

    List<BookProfileResponse> getAllMembers(Long memberId, MemberBookProfileRequestDto requestDto);

    List<Member> getRecommendationMembers(MemberRecommendationResponse memberRecommendationResponse, List<Postcard> postcards);
}
