package com.bookbla.americano.domain.test.service.impl;

import com.bookbla.americano.domain.test.controller.dto.request.TestRequestDTO;
import com.bookbla.americano.domain.test.controller.dto.response.TestResponseDTO;
import com.bookbla.americano.domain.test.repository.TestRepository;
import com.bookbla.americano.domain.test.repository.entity.TestEntity;
import com.bookbla.americano.domain.test.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final TestRepository testRepository;

    @Override
    @Transactional
    public TestResponseDTO test(TestRequestDTO requestDTO) {

        TestEntity testEntity = TestRequestDTO.toEntity(requestDTO);

        return TestResponseDTO.fromEntity(testRepository.save(testEntity));
    }
}
