package com.bookbla.americano.domain.test.controller;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.base.exception.BaseExceptionType;
import com.bookbla.americano.domain.test.controller.dto.request.TestRequestDTO;
import com.bookbla.americano.domain.test.controller.dto.response.TestResponseDTO;
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

    @GetMapping("/{contents}")
    public ResponseEntity<List<TestResponseDTO>> test(@RequestParam("contents") String contents) {
        List<TestResponseDTO> listByContents = testService.getListByContents(contents);
        return ResponseEntity.ok(listByContents);
    }

    @PostMapping("")
    public ResponseEntity<TestResponseDTO> testSave(@RequestBody @Valid TestRequestDTO requestDTO) {
        TestResponseDTO testResponseDTO = testService.test(requestDTO);
        return ResponseEntity.created(URI.create("/test/" + testResponseDTO.getId()))
                .body(testResponseDTO);
    }

    @GetMapping("/error")
    public void testError() {
        throw new BaseException(BaseExceptionType.TEST_FAIL);
    }
}
