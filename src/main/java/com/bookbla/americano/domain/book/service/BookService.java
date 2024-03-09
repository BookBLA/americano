package com.bookbla.americano.domain.book.service;

import com.bookbla.americano.domain.book.service.dto.BookSearchResponses;

public interface BookService {

    BookSearchResponses searchBook(String text, int size, int page);
}
