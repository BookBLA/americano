package com.bookbla.americano.domain.test.service;

import com.bookbla.americano.domain.test.controller.dto.request.TestRequestDTO;
import com.bookbla.americano.domain.test.controller.dto.response.TestResponseDTO;

public interface TestService {
    TestResponseDTO test(TestRequestDTO requestDTO);
}
