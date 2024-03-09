package com.bookbla.americano.domain.book.service;

import com.bookbla.americano.domain.book.service.dto.BookSearchResponses;

public interface BookProvider {

    BookSearchResponses searchBooks(String text, int size, int page);

}
