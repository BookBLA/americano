package com.bookbla.americano.domain.test.controller.dto.request;

import com.bookbla.americano.domain.test.repository.entity.TestEntity;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TestRequestDTO {

    @NotNull(message = "내용이 입력되지 않았습니다")
    private String contents;

    public TestEntity toEntity() {
        return TestEntity.builder()
                .contents(contents)
                .build();
    }
}
