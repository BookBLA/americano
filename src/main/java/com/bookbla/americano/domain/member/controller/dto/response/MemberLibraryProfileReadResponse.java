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
    private final Long memberProfileId;
    private final String name;
    private final int age;
    private final String gender;
    private final String school;
    private final String profileImageUrl;
    private final boolean isMatched;
    private final List<BookResponse> bookResponses;

    @Getter
    @AllArgsConstructor
    public static class BookResponse {

        private final Long memberBookId;
        private final String bookImageUrl;

    }

    public static MemberLibraryProfileReadResponse of(Member member, MemberProfile memberProfile, List<MemberBook> memberBooks) {
        return of(member, memberProfile, memberBooks, false); // 기본값으로 false를 사용
    }

    public static MemberLibraryProfileReadResponse of(Member member, MemberProfile memberProfile, List<MemberBook> memberBooks, boolean isMatched) {
        List<BookResponse> bookResponses = memberBooks.stream()
                .map(it -> new BookResponse(it.getId(), it.getBook().getImageUrl()))
                .collect(Collectors.toList());
        return new MemberLibraryProfileReadResponse(
                member.getId(),
                memberProfile.getId(),
                memberProfile.showBlindName(),
                memberProfile.calculateAge(LocalDate.now()),
                memberProfile.getGender().name(),
                memberProfile.getSchoolName(),
                memberProfile.getProfileImageUrl(),
                isMatched,
                bookResponses
        );
    }
}
