package com.bookbla.americano.domain.book.infra.kakao.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.bookbla.americano.domain.book.service.dto.BookSearchResponses;
import com.bookbla.americano.domain.book.service.dto.BookSearchResponses.BookSearchResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoBookResponse {

    private List<Document> documents;
    private Meta meta;

    @Getter
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Document {

        private String title;
        private List<String> authors;
        private String isbn;
        private String thumbnail;

    }

    @Getter
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    static class Meta {

        private boolean isEnd;
        private int pageableCount;
        private int totalCount;

    }

    public BookSearchResponses toBookSearchResponsesWith(int page) {
        List<BookSearchResponse> bookSearchResponses = documents.stream()
                .map(it -> new BookSearchResponse(it.title, it.authors, toIsbn13(it.getIsbn()), it.thumbnail))
                .distinct()
                .collect(Collectors.toList());
        return BookSearchResponses.builder()
                .totalCount(getMeta().pageableCount)
                .page(page)
                .isEnd(getMeta().isEnd())
                .bookSearchResponses(bookSearchResponses)
                .build();
    }

    private String toIsbn13(String rawIsbn) {
        if (rawIsbn.contains(" ")) {
            return rawIsbn.split(" ")[1];
        }
        return rawIsbn;
    }
}
