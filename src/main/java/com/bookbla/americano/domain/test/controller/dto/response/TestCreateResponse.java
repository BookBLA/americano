package com.bookbla.americano.domain.test.controller.dto.response;

import com.bookbla.americano.domain.test.repository.entity.TestEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TestCreateResponse {

    private final Long id;

    public static TestCreateResponse from(TestEntity entity) {
        return new TestCreateResponse(entity.getId());
    }
}
