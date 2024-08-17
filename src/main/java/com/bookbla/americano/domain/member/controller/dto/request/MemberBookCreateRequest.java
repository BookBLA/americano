package com.bookbla.americano.domain.member.controller.dto.request;

import java.util.List;

import com.bookbla.americano.domain.book.repository.entity.Book;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import com.bookbla.americano.domain.quiz.repository.entity.QuizQuestion;
import javax.validation.constraints.NotBlank;
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

    @NotBlank(message = "리뷰가 입력되지 않았습니다.")
    private String review;

    @NotBlank(message = "퀴즈가 입력되지 않았습니다.")
    private String quiz;

    @NotBlank(message = "퀴즈 정답이 입력되지 않았습니다.")
    private String quizAnswer;

    @NotBlank(message = "퀴즈 답안이 입력되지 않았습니다.")
    private String firstWrongChoice;

    @NotBlank(message = "퀴즈 답안이 입력되지 않았습니다.")
    private String secondWrongChoice;

    public Book toBook() {
        return Book.builder()
                .title(title)
                .isbn(isbn)
                .authors(authors)
                .build();
    }

    public QuizQuestion toQuizQuestion(MemberBook memberBook) {
        return QuizQuestion.builder()
                .memberBook(memberBook)
                .contents(quiz)
                .firstChoice(quizAnswer)
                .secondChoice(firstWrongChoice)
                .thirdChoice(secondWrongChoice)
                .build();
    }

    public MemberBook toMemberBook(Book book, Member member) {
        return MemberBook.builder()
                .book(book)
                .isRepresentative(isRepresentative)
                .review(review)
                .member(member)
                .build();
    }
}
