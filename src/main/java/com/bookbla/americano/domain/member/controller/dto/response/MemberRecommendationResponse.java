package com.bookbla.americano.domain.member.controller.dto.response;

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
public class MemberRecommendationResponse {
    private Long memberId;
    private Gender memberGender;
    private String memberSchoolName;
    private SmokeType memberSmokeType;
    private Set<Long> excludeMemberIds;

    private final List<RecommendationBookResponse> recommendationBookResponses;

    @Getter
    @Builder
    public static class RecommendationBookResponse {
        private final String bookTitle;
        private final List<String> bookAuthors;
        private final String bookIsbn;

    }

    public static MemberRecommendationResponse from(Member member, List<MemberBook> memberBooks) {

        List<RecommendationBookResponse> bookResponses = memberBooks.stream()
                .map(memberBook -> RecommendationBookResponse.builder()
                        .bookTitle(memberBook.getBook().getTitle())
                        .bookAuthors(memberBook.getBook().getAuthors())
                        .bookIsbn(memberBook.getBook().getIsbn())
                        .build())
                .collect(Collectors.toList());


        return MemberRecommendationResponse.builder()
                .memberId(member.getId())
                .memberGender(member.getMemberProfile().getGender())
                .memberSchoolName(member.getSchool().getName())
                .memberSmokeType(member.getMemberStyle().getSmokeType())
                .excludeMemberIds(member.getMemberMatchIgnores())
                .recommendationBookResponses(bookResponses)
                .build();
    }

}
