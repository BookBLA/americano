package com.bookbla.americano.domain.book.repository.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.bookbla.americano.base.entity.BaseEntity;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = @Index(name = "UK_ehpdfjpu1jm3hijhj4mm0hx9h", columnList = "isbn", unique = true))
public class Book extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String isbn;

    private String title;

    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "book_authors", joinColumns = @JoinColumn(name = "book_id"),
            indexes = @Index(name = "FKs4xm7q8i3uxvaiswj1c35nnxw", columnList = "authors"))
    @Column(name = "authors")
    private List<String> authors = new ArrayList<>();

    private String imageUrl;

    public void updateImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(isbn, book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(isbn);
    }
}
