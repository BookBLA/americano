package com.bookbla.americano.domain.test.controller;

import com.bookbla.americano.base.exceptions.BizException;
import com.bookbla.americano.domain.test.controller.dto.request.TestRequestDTO;
import com.bookbla.americano.domain.test.controller.dto.response.TestResponseDTO;
import com.bookbla.americano.domain.test.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @GetMapping("")
    public String test() {
        return "test contorller";
    }

    @PostMapping("")
    public TestResponseDTO testSave(@RequestBody TestRequestDTO requestDTO) {
        return testService.test(requestDTO);
    }

    @GetMapping("/error")
    public void testError() {
        throw new BizException("error test");
    }
}
