package com.bookbla.americano.domain.test.service.impl;

import com.bookbla.americano.domain.test.controller.dto.request.TestRequestDTO;
import com.bookbla.americano.domain.test.controller.dto.response.TestResponseDTO;
import com.bookbla.americano.domain.test.service.TestService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TestServiceImplTest {

    @Autowired
    private TestService testService;

    @Test
    public void 테스트의_테스트() {

        //given
        TestRequestDTO requestDTO = TestRequestDTO.builder()
                .contents("test")
                .build();

        //when
        TestResponseDTO result = testService.test(requestDTO);

        //then
        Assertions.assertThat(requestDTO.getContents()).isEqualTo(result.getContents());
    }
}