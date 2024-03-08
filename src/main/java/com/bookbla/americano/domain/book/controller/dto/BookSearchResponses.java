package com.bookbla.americano.domain.book.controller.dto;

import com.bookbla.americano.domain.book.service.dto.BookInformationResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BookSearchResponses {

    private final int totalCount;
    private final List<BookSearchResponse> bookSearchResponses;

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    static class BookSearchResponse {
        private final String title;
        private final List<String> authors;
        private final String isbn;
        private final String imageUrl;
    }

    public static BookSearchResponses from(List<BookInformationResponse> bookInformationResponses) {
        List<BookSearchResponse> bookSearchResponses = bookInformationResponses.stream()
                .map(BookSearchResponses::from)
                .collect(Collectors.toList());

        return new BookSearchResponses(bookInformationResponses.size(), bookSearchResponses);
    }

    private static BookSearchResponse from(BookInformationResponse bookInformationResponse) {
        return new BookSearchResponse(
                bookInformationResponse.getTitle(),
                bookInformationResponse.getAuthors(),
                bookInformationResponse.getIsbn(),
                bookInformationResponse.getImageUrl()
        );
    }
}