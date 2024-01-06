package com.bookbla.americano.domain.test.controller.dto.request;

import com.bookbla.americano.domain.test.repository.entity.TestEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class TestRequestDTO {

    private final String contents;

    public static TestEntity toEntity(TestRequestDTO requestDTO) {
        return TestEntity.builder()
                .contents(requestDTO.getContents())
                .build();
    }
}
