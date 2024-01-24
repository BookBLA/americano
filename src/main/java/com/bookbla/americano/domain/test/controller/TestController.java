package com.bookbla.americano.domain.test.controller;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.base.exception.BaseExceptionType;
import com.bookbla.americano.domain.test.controller.dto.request.TestRequestDto;
import com.bookbla.americano.domain.test.controller.dto.response.TestResponseDto;
import com.bookbla.americano.domain.test.service.TestService;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @GetMapping
    public ResponseEntity<List<TestResponseDto>> readTest(@RequestParam String contents) {
        List<TestResponseDto> testResponses = testService.findTestsByContents(contents);
        return ResponseEntity.ok(testResponses);
    }

    @PostMapping
    public ResponseEntity<TestResponseDto> createTest(@RequestBody @Valid TestRequestDto testRequestDto) {
        TestResponseDto testResponseDto = testService.create(testRequestDto);
        return ResponseEntity.created(URI.create("/test/" + testResponseDto.getId()))
                .body(testResponseDto);
    }

    @GetMapping("/error")
    public void testError() {
        throw new BaseException(BaseExceptionType.TEST_FAIL);
    }
}
