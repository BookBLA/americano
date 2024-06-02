package com.bookbla.americano.domain.postcard.controller.dto.response;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.bookbla.americano.domain.postcard.enums.PostcardStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PostcardStatusResponseTest {

    @Test
    void JSON_VALUE를_이용하여_직렬화_할_수_있다() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        PostcardStatusResponse response = PostcardStatusResponse.from(PostcardStatus.READ);
        String json = objectMapper.writeValueAsString(response);

        assertEquals(json, "{\"postcardStatus\":\"READ\"}");
    }
}