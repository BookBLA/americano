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
    private List<Item> item;

    @Data
    @Getter
    @NoArgsConstructor
    public static class Item {

        private String title;
        private String author;
        private String cover; // 프로필 이미지

    }

    public boolean hasNotCover() {
        return item == null || item.isEmpty();
    }
}
