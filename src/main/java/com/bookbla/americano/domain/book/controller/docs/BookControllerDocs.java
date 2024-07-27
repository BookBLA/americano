package com.bookbla.americano.domain.book.controller.docs;

import com.bookbla.americano.domain.book.service.dto.BookSearchResponses;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface BookControllerDocs {

    @Operation(summary = "작가 혹은 도서를 검색합니다")
    @GetMapping
    ResponseEntity<BookSearchResponses> searchBooks(
            @RequestParam String text,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "1") int page
    );

}
