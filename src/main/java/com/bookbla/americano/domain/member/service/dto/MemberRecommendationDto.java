package com.bookbla.americano.domain.member.service.dto;

import com.bookbla.americano.domain.member.enums.Gender;
import com.bookbla.americano.domain.member.enums.SmokeType;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
@Getter
public class MemberRecommendationDto {
    private Long memberId;
    private String memberGender;
    private Long memberSchoolId;
    private String memberSmokeType;
    private Set<Long> excludeMemberIds;

    private final List<RecommendationBookDto> recommendationBookDtoList;

    @Getter
    @Builder
    public static class RecommendationBookDto {
        private final String bookTitle;
        private final List<String> bookAuthors;
    }

    public static MemberRecommendationDto from(Member member, List<MemberBook> memberBooks) {

        List<RecommendationBookDto> bookResponses = memberBooks.stream()
                .map(memberBook -> RecommendationBookDto.builder()
                        .bookTitle(memberBook.getBook().getTitle())
                        .bookAuthors(memberBook.getBook().getAuthors())
                        .build())
                .collect(Collectors.toList());


        return MemberRecommendationDto.builder()
                .memberId(member.getId())
                .memberGender(member.getMemberProfile().getGenderName())
                .memberSchoolId(member.getSchool().getId())
                .memberSmokeType(member.getMemberStyle().getSmokeTypeName())
                .excludeMemberIds(member.getMemberMatchIgnores())
                .recommendationBookDtoList(bookResponses)
                .build();
    }

}
