package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.enums.Gender;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
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

    public static MemberIntroResponse from(Member member, MemberBook memberBook) {
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
                .build();
    }
}
