package com.bookbla.americano.domain.book.controller;

import com.bookbla.americano.domain.book.controller.docs.BookControllerDocs;
import com.bookbla.americano.domain.book.service.BookService;
import com.bookbla.americano.domain.book.service.dto.BookSearchResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController implements BookControllerDocs {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<BookSearchResponses> searchBooks(
            @RequestParam String text,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "1") int page
    ) {
        BookSearchResponses bookSearchResponses = bookService.searchBook(text, size, page);
        return ResponseEntity.ok(bookSearchResponses);
    }

}
