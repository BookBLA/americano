package com.bookbla.americano.domain.book.infra.kakao.dto;

import com.bookbla.americano.domain.book.service.dto.BookInformationResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import java.util.stream.Collectors;
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

        private String isEnd;
        private int pageableCount;
        private int totalCount;

    }

    public List<BookInformationResponse> toBookInformationResponse() {
        return this.documents.stream()
                .map(it -> new BookInformationResponse(it.title, it.authors, it.isbn, it.thumbnail))
                .collect(Collectors.toList());
    }
}
