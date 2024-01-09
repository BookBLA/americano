package com.bookbla.americano.domain.test.controller;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.base.exception.BaseExceptionType;
import com.bookbla.americano.domain.test.controller.dto.request.TestRequestDTO;
import com.bookbla.americano.domain.test.controller.dto.response.TestResponseDTO;
import com.bookbla.americano.domain.test.service.TestService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/{contents}")
    public List<TestResponseDTO> test(@RequestParam("contents") String contents) {
        return testService.getListByContents(contents);
    }

    @PostMapping("")
    public TestResponseDTO testSave(@RequestBody @Valid TestRequestDTO requestDTO) {
        return testService.test(requestDTO);
    }

    @GetMapping("/error")
    public void testError() {
        throw new BaseException(BaseExceptionType.TEST_FAIL);
    }
}
