package com.bookbla.americano.domain.test.service.dto;

import com.bookbla.americano.domain.test.repository.entity.TestEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TestDto {

    private final String contents;

    public TestEntity toEntity() {
        return TestEntity.builder()
                .contents(contents)
                .build();
    }
}
