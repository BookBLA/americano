package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.book.repository.entity.Book;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberBookReadResponse {

    private final String title;
    private final Set<String> authors;
    private final String imageUrl;
    private final String review;

    public static MemberBookReadResponse of(MemberBook memberBook) {
        Book book = memberBook.getBook();
        return new MemberBookReadResponse(
                book.getTitle(),
                book.getAuthors(),
                book.getImageUrl(),
                memberBook.getReview()
        );
    }
}
