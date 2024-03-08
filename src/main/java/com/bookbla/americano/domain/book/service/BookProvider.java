package com.bookbla.americano.domain.book.service;

import com.bookbla.americano.domain.book.service.dto.BookInformationResponse;
import java.util.List;

public interface BookProvider {

    List<BookInformationResponse> searchBooks(String text, int size, int page);

}
