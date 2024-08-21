package com.bookbla.americano.domain.book.infra.aladin;

import java.util.Optional;

import com.bookbla.americano.domain.book.infra.aladin.dto.AladinBookResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static io.netty.util.internal.ThrowableUtil.stackTraceToString;

@Component
@Slf4j
public class AladinBookClient {

    private static final String IMG_SIZE = "Big";
    private static final String OUTPUT_FORMAT = "js";
    private static final String ITEM_ID_TYPE = "ISBN13";

    @Value("${book.aladin.api-key}")
    private String key;

    private final AladinBookApi aladinBookApi;
    private final ObjectMapper objectMapper;

    public AladinBookClient(AladinBookApi aladinBookApi, ObjectMapper objectMapper) {
        this.aladinBookApi = aladinBookApi;
        this.objectMapper = objectMapper;
    }

    public Optional<String> findImageByIsbn13(String isbn) {
        AladinBookResponse aladinResponse = null;
        String response = aladinBookApi.getBook(key, isbn, ITEM_ID_TYPE, OUTPUT_FORMAT, IMG_SIZE);

        try {
            aladinResponse = objectMapper.readValue(response, AladinBookResponse.class);
        } catch (JsonProcessingException e) {
            log.info(e.getMessage());
            log.info(stackTraceToString(e));
            return Optional.empty();
        }
        if (aladinResponse.hasNotCover()) {
            return Optional.empty();
        }

        return aladinResponse.getItem()
                .stream()
                .map(AladinBookResponse.Item::getCover)
                .findFirst();
    }
}
