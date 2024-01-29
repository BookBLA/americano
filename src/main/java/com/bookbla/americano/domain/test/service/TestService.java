package com.bookbla.americano.domain.test.service;

import com.bookbla.americano.domain.test.controller.dto.response.TestCreateResponse;
import com.bookbla.americano.domain.test.controller.dto.response.TestReadResponse;
import com.bookbla.americano.domain.test.service.dto.TestDto;
import java.util.List;

public interface TestService {

    TestCreateResponse create(TestDto testDto);

    List<TestReadResponse> findTestsByContents(String contents);
}
