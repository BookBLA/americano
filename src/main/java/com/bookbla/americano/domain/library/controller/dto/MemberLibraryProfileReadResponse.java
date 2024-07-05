package com.bookbla.americano.domain.library.controller.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberLibraryProfileReadResponse {

    private final Long memberId;
    private final String name;
    private final int age;
    private final String gender;
    private final String school;
    private final String openKakaoRoomStatus;
    private final String studentIdStatus;
    private final String profileImageStatus;
    private final String profileImageUrl;
    private final List<BookResponse> bookResponses;

    @Getter
    @RequiredArgsConstructor
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
                memberProfile.getName(),
                memberProfile.calculateAge(LocalDate.now()),
                memberProfile.getGender().name(),
                member.getSchool().getName(),
                memberProfile.getOpenKakaoRoomStatus().name(),
                memberProfile.getStudentIdImageStatus().name(),
                memberProfile.getProfileImageStatus().name(),
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
                memberProfile.getName(),
                memberProfile.calculateAge(LocalDate.now()),
                memberProfile.getGender().name(),
                member.getSchool().getName(),
                memberProfile.getOpenKakaoRoomStatus().name(),
                memberProfile.getStudentIdImageStatus().name(),
                memberProfile.getProfileImageStatus().name(),
                memberProfile.getProfileImageUrl(),
                bookResponses
        );
    }
}
