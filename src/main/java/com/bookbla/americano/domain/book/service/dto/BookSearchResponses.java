package com.bookbla.americano.domain.book.service.dto;


import java.util.List;
import java.util.Objects;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof BookSearchResponse)) return false;
            final BookSearchResponse that = (BookSearchResponse) o;
            return Objects.equals(isbn, that.isbn);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(isbn);
        }
    }

    public boolean getIsEnd() {
        return isEnd;
    }
}
