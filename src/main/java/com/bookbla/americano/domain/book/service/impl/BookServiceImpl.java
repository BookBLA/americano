package com.bookbla.americano.domain.book.service.impl;

import com.bookbla.americano.domain.book.service.dto.BookSearchResponses;
import com.bookbla.americano.domain.book.service.BookProvider;
import com.bookbla.americano.domain.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookProvider bookProvider;

    @Override
    public BookSearchResponses searchBook(String text, int size, int page) {
        return bookProvider.searchBooks(text, size, page);
    }

}
