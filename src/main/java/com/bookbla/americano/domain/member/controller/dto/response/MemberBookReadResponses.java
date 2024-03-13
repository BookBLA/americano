package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class MemberBookReadResponses {

    private final List<MemberBookReadResponse> memberBookReadResponses;

    @Getter
    @RequiredArgsConstructor
    public static class MemberBookReadResponse {

        private final Long memberBookId;
        private final boolean isRepresentative;
        private final String title;
        private final String thumbnail;
        private final Set<String> authors;

    }

    public static MemberBookReadResponses from(List<MemberBook> memberBooks) {
        List<MemberBookReadResponses.MemberBookReadResponse> memberBookReadResponses = memberBooks.stream()
                .map(memberBook -> new MemberBookReadResponses.MemberBookReadResponse(
                        memberBook.getId(),
                        memberBook.isRepresentative(),
                        memberBook.getBook().getTitle(),
                        memberBook.getBook().getImageUrl(),
                        memberBook.getBook().getAuthors()
                )).collect(Collectors.toList());
        return MemberBookReadResponses.builder()
                .memberBookReadResponses(memberBookReadResponses)
                .build();
    }

}
