package com.bookbla.americano.domain.member.controller.dto.request;

import com.bookbla.americano.domain.book.repository.entity.Book;

import java.util.List;
import java.util.Set;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberBookCreateRequest {

    @NotNull(message = "대표책 여부가 입력되지 않았습니다.")
    private Boolean isRepresentative;

    @NotNull(message = "책 제목이 입력되지 않았습니다.")
    private String title;

    @NotNull(message = "책 작가가 입력되지 않았습니다.")
    private List<String> authors;

    @NotNull(message = "도서번호가 입력되지 않았습니다.")
    private String isbn;

    @NotNull(message = "이미지 링크가 입력되지 않았습니다.")
    private String thumbnail;

    public Book toBook() {
        return Book.builder()
                .title(title)
                .isbn(isbn)
                .authors(authors)
                .imageUrl(thumbnail)
                .build();
    }

}
