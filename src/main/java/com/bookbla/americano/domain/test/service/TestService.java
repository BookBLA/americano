package com.bookbla.americano.domain.test.service;

import com.bookbla.americano.domain.test.controller.dto.request.TestCreateRequest;
import com.bookbla.americano.domain.test.controller.dto.response.TestCreateResponse;
import com.bookbla.americano.domain.test.controller.dto.response.TestReadResponse;
import java.util.List;

public interface TestService {

    TestCreateResponse create(TestCreateRequest testCreateRequest);

    List<TestReadResponse> findTestsByContents(String contents);
}
