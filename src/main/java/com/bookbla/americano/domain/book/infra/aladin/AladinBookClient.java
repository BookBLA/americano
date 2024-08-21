package com.bookbla.americano.domain.book.infra.aladin;

import java.util.Optional;

import com.bookbla.americano.domain.book.infra.aladin.dto.AladinBookResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
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

    public Optional<String> findImageByIsbn13(String isbn) {
        AladinBookResponse aladinResponse = null;
        String response = aladinBookApi.getBook(key, isbn, ITEM_ID_TYPE, OUTPUT_FORMAT, IMG_SIZE);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            aladinResponse = objectMapper.readValue(response, AladinBookResponse.class);
        } catch (JsonProcessingException e) {
            log.info(e.getMessage());
            return Optional.empty();
        }

        return aladinResponse.getItem()
                .stream()
                .map(AladinBookResponse.Item::getCover)
                .findFirst();
    }
}
