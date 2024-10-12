package com.bookbla.americano.domain.matching.controller.dto.response;

import com.bookbla.americano.domain.matching.repository.entity.MemberMatching;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class MemberIntroResponse {

    private Long memberId;
    private String memberProfileImageUrl;
    private String memberName;
    private int memberAge;
    private String memberGender;
    private String memberSchoolName;

    private Long memberBookId;
    private String bookCoverImageUrl;
    private String bookTitle;
    private List<String> bookAuthors;
    private String review;

    private Boolean isInvitationCard;

    public static MemberIntroResponse from(Member member, MemberBook memberBook, MemberMatching memberMatching) {
        return MemberIntroResponse.builder()
                .memberId(member.getId())
                .memberProfileImageUrl(member.getMemberStyle().getProfileImageType().getImageUrl())
                .memberName(member.getMemberProfile().getName())
                .memberAge(member.getMemberProfile().calculateAge(LocalDate.now()))
                .memberGender(member.getMemberProfile().getGenderName())
                .memberSchoolName(member.getSchool().getName())

                .memberBookId(memberBook.getId())
                .bookCoverImageUrl(memberBook.getBook().getImageUrl())
                .bookTitle(memberBook.getBook().getTitle())
                .bookAuthors(memberBook.getBook().getAuthors())
                .review(memberBook.getReview())
                .isInvitationCard(memberMatching.getIsInvitationCard())
                .build();
    }

    public static MemberIntroResponse empty() {
        return MemberIntroResponse.builder().build();
    }
}
