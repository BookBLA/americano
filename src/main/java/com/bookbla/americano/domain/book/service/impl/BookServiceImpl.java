package com.bookbla.americano.domain.book.service.impl;

import com.bookbla.americano.domain.book.controller.dto.BookSearchResponses;
import com.bookbla.americano.domain.book.service.BookProvider;
import com.bookbla.americano.domain.book.service.BookService;
import com.bookbla.americano.domain.book.service.dto.BookInformationResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookProvider bookProvider;

    public BookSearchResponses searchBook(String text, int size, int page) {
        List<BookInformationResponse> bookInformationResponses = bookProvider.searchBooks(text, size, page);
        return BookSearchResponses.from(bookInformationResponses);
    }

}
