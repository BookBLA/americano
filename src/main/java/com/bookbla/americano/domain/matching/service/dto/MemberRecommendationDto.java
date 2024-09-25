package com.bookbla.americano.domain.matching.service.dto;

import com.bookbla.americano.domain.member.repository.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberRecommendationDto {

    private Long memberId;
    private String memberGender;

    public static MemberRecommendationDto from(Member member) {
        return MemberRecommendationDto.builder()
                .memberId(member.getId())
                .memberGender(member.getMemberProfile().getGender().name())
                .build();
    }
}
