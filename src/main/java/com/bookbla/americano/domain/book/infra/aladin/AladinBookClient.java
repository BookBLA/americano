package com.bookbla.americano.domain.book.infra.aladin;

import com.bookbla.americano.domain.book.infra.aladin.dto.AladinBookResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AladinBookClient {

    private static final String IMG_SIZE = "Big";
    private static final String OUTPUT_FORMAT = "js";
    private static final String ITEM_ID_TYPE = "ISBN13";

    @Value("${book.aladin.api-key}")
    private String key;

    private final AladinBookApi aladinBookApi;

    public AladinBookClient(AladinBookApi aladinBookApi) {
        this.aladinBookApi = aladinBookApi;
    }

    public AladinBookResponse findByIsbn13(String isbn) {
        return aladinBookApi.getBook(key, isbn, ITEM_ID_TYPE, OUTPUT_FORMAT, IMG_SIZE);
    }
}
