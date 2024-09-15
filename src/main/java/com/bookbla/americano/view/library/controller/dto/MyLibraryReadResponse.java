package com.bookbla.americano.view.library.controller.dto;

import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import com.bookbla.americano.domain.member.repository.entity.MemberStyle;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MyLibraryReadResponse {

    private final Long memberId;
    private final String name;
    private final int age;
    private final String gender;
    private final String school;
    private final String profileImageUrl;
    private final String mbti;
    private final String smokeType;
    private final int height;
    private final List<BookResponse> bookResponses;

    public static MyLibraryReadResponse of(Member member, List<MemberBook> memberBooks) {
        List<BookResponse> bookResponses = memberBooks.stream()
                .map(it -> new BookResponse(it.getId(), it.getBook().getImageUrl()))
                .collect(Collectors.toList());
        MemberProfile memberProfile = member.getMemberProfile();
        MemberStyle memberStyle = member.getMemberStyle();
        return new MyLibraryReadResponse(
                member.getId(),
                memberProfile.getName(),
                memberProfile.calculateAge(LocalDate.now()),
                memberProfile.getGender().name(),
                member.getSchool().getName(),
                memberStyle.getProfileImageType().getImageUrl(),
                memberStyle.getMbti().name(),
                memberStyle.getSmokeType().getValue(),
                memberStyle.getHeight(),
                bookResponses
        );
    }

    @Getter
    @RequiredArgsConstructor
    public static class BookResponse {

        private final Long memberBookId;
        private final String bookImageUrl;

    }
}
