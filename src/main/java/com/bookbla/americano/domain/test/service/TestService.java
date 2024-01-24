package com.bookbla.americano.domain.test.service;

import com.bookbla.americano.domain.test.controller.dto.request.TestRequestDto;
import com.bookbla.americano.domain.test.controller.dto.response.TestResponseDto;
import java.util.List;

public interface TestService {

    TestResponseDto create(TestRequestDto testRequestDto);

    List<TestResponseDto> findTestsByContents(String contents);
}
