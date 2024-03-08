package com.bookbla.americano.domain.book.repository;

import com.bookbla.americano.domain.book.Book;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

}
