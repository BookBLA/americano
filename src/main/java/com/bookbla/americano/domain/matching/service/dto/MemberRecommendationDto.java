package com.bookbla.americano.domain.matching.service.dto;

import com.bookbla.americano.domain.member.repository.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
public class MemberRecommendationDto {

    private Long memberId;
    private String memberGender;
    private Set<Long> excludeMemberIds;

    public static MemberRecommendationDto from(Member member, Set<Long> excludeMemberIds) {
        return MemberRecommendationDto.builder()
                .memberId(member.getId())
                .memberGender(member.getMemberProfile().getGender().name())
                .excludeMemberIds(excludeMemberIds)
                .build();
    }
}
