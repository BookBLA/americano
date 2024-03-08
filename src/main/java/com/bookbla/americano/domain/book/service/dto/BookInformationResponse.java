package com.bookbla.americano.domain.book.service.dto;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BookInformationResponse {

    private final String title;
    private final List<String> authors;
    private final String isbn;
    private final String imageUrl;

}
