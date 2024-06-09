package com.bookbla.americano.domain.test.controller.dto.response;

import com.bookbla.americano.domain.test.repository.entity.TestEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TestReadResponse {

    private final Long id;
    private final String contents;

    public static TestReadResponse from(TestEntity entity) {
        return TestReadResponse.builder()
                .id(entity.getId())
                .contents(entity.getContents())
                .build();
    }
}
