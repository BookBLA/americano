package com.bookbla.americano.domain.test.service.impl;

import com.bookbla.americano.domain.test.controller.dto.request.TestRequestDto;
import com.bookbla.americano.domain.test.controller.dto.response.TestResponseDto;
import com.bookbla.americano.domain.test.repository.TestRepository;
import com.bookbla.americano.domain.test.repository.entity.TestEntity;
import com.bookbla.americano.domain.test.service.TestService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final TestRepository testRepository;

    @Override
    @Transactional
    public TestResponseDto create(TestRequestDto testRequestDto) {
        TestEntity testEntity = testRequestDto.toEntity();
        return TestResponseDto.from(testRepository.save(testEntity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TestResponseDto> findTestsByContents(String contents) {
        return testRepository.findByContents(contents).stream()
                .map(TestResponseDto::from)
                .collect(Collectors.toList());
    }
}
