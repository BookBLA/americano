package com.bookbla.americano.domain.book.service.dto;


import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BookSearchResponses {

    private final int totalCount;
    private final int page;

    @Getter(AccessLevel.NONE)
    private final boolean isEnd;
    private final List<BookSearchResponse> bookSearchResponses;

    @Getter
    @RequiredArgsConstructor
    public static class BookSearchResponse {

        private final String title;
        private final List<String> authors;
        private final String isbn;
        private final String imageUrl;

    }

    public boolean getIsEnd() {
        return isEnd;
    }
}
