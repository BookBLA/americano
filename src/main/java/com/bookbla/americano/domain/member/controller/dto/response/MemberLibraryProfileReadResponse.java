package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberLibraryProfileReadResponse {

    private final Long memberId;
    private final String name;
    private final int age;
    private final String gender;
    private final String school;
    private final String profileImageUrl;
    private final List<BookResponse> bookResponses;

    @Getter
    @AllArgsConstructor
    public static class BookResponse {
        private final Long memberBookId;
        private final boolean isRepresentative;
        private final String bookImageUrl;

    }

    public static MemberLibraryProfileReadResponse ofPendingProfileImage(Member member, List<MemberBook> memberBooks, String pendingProfileImageUrl) {
        List<BookResponse> bookResponses = memberBooks.stream()
                .map(it -> new BookResponse(it.getId(), it.isRepresentative(), it.getBook().getImageUrl()))
                .collect(Collectors.toList());
        MemberProfile memberProfile = member.getMemberProfile();
        return new MemberLibraryProfileReadResponse(
                member.getId(),
                memberProfile.showBlindName(),
                memberProfile.calculateAge(LocalDate.now()),
                memberProfile.getGender().name(),
                memberProfile.getSchoolName(),
                pendingProfileImageUrl,
                bookResponses
        );
    }

    public static MemberLibraryProfileReadResponse of(Member member, List<MemberBook> memberBooks) {
        List<BookResponse> bookResponses = memberBooks.stream()
                .map(it -> new BookResponse(it.getId(), it.isRepresentative(), it.getBook().getImageUrl()))
                .collect(Collectors.toList());
        MemberProfile memberProfile = member.getMemberProfile();
        return new MemberLibraryProfileReadResponse(
                member.getId(),
                memberProfile.showBlindName(),
                memberProfile.calculateAge(LocalDate.now()),
                memberProfile.getGender().name(),
                memberProfile.getSchoolName(),
                memberProfile.getProfileImageUrl(),
                bookResponses
        );
    }
}
