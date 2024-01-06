package com.bookbla.americano.domain.test.controller.dto.response;

import com.bookbla.americano.domain.test.repository.entity.TestEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TestResponseDTO {
    private final Long id;
    private final String contents;

    public static TestResponseDTO fromEntity(TestEntity entity) {
        return TestResponseDTO.builder()
                .id(entity.getId())
                .contents(entity.getContents())
                .build();
    }
}
