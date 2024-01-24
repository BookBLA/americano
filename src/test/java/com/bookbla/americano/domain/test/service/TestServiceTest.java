package com.bookbla.americano.domain.test.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.bookbla.americano.domain.test.controller.dto.request.TestRequestDto;
import com.bookbla.americano.domain.test.controller.dto.response.TestResponseDto;
import com.bookbla.americano.domain.test.repository.TestRepository;
import com.bookbla.americano.domain.test.service.TestService;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TestServiceTest {

    @Autowired
    private TestService testService;

    @Autowired
    private TestRepository testRepository;

    @Test
    void 테스트를_생성할_수_있다() {
        //given
        TestRequestDto testRequestDto = new TestRequestDto("test");

        //when
        TestResponseDto actual = testService.create(testRequestDto);

        //then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getContents()).isEqualTo("test")
        );
    }

    @Test
    void 내용으로_테스트를_찾을_수_있다() {
        //given
        testService.create(new TestRequestDto("찾을 놈"));
        testService.create(new TestRequestDto("못찾을 놈"));

        //when
        List<TestResponseDto> actual = testService.findTestsByContents("찾을 놈");

        //then
        assertAll(
                () -> assertThat(actual.size()).isOne(),
                () -> assertThat(actual).extracting("contents").contains("찾을 놈"),
                () -> assertThat(actual).extracting("contents").doesNotContain("못찾을 놈")
        );
    }

    @AfterEach
    void deleteAll() {
        testRepository.deleteAll();
    }
}
