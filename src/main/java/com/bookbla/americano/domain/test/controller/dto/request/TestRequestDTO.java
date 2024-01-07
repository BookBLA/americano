package com.bookbla.americano.domain.test.controller.dto.request;

import com.bookbla.americano.domain.test.repository.entity.TestEntity;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestRequestDTO {

    private String contents;

    public static TestEntity toEntity(TestRequestDTO requestDTO) {
        return TestEntity.builder()
                .contents(requestDTO.getContents())
                .build();
    }
}
