package com.bookbla.americano.domain.member.controller;


import com.bookbla.americano.domain.member.controller.docs.StyleControllerDocs;
import com.bookbla.americano.domain.member.controller.dto.response.StylesResponse;
import com.bookbla.americano.domain.member.service.StyleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/styles")
public class StyleController implements StyleControllerDocs {

    private final StyleService styleService;

    @GetMapping
    public ResponseEntity<StylesResponse> readStyles() {
        return ResponseEntity.ok(styleService.readStyles());
    }

}
