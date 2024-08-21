package com.bookbla.americano.domain.book.infra.aladin.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AladinBookResponse {

    private String version;
    private String title;
    private String link;
    private String pubDate;
    private String imageUrl;
    private int totalResults;
    private int startIndex;
    private int itemsPerPage;
    private String query;
    private int searchCategoryId;
    private String searchCategoryName;
    private List<Item> item;

    @Data
    @Getter
    @NoArgsConstructor
    public static class Item {

        private String title;
        private String link;
        private String author;
        private String pubDate;
        private String description;
        private String creator;
        private String isbn;
        private String isbn13;
        private int itemId;
        private int priceSales;
        private int priceStandard;
        private String stockStatus;
        private int mileage;
        private String cover; // 프로필 이미지
        private int categoryId;
        private String categoryName;
        private String publisher;
        private int customerReviewRank;
        private BookInfo bookinfo;

        @Data
        @NoArgsConstructor
        static class BookInfo {

            private String subTitle;
            private String originalTitle;
            private int itemPage;
            private String toc;
            private List<String> letslookimg;
            private List<Author> authors;
            private List<String> ebookList;

            @Data
            @NoArgsConstructor
            static class Author {

                private String authorType;
                private int authorid;
                private String desc;
                private String name;

            }
        }
    }
}
