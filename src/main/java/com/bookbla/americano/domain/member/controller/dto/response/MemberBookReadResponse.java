package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.book.repository.entity.Book;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;

import java.util.List;

import com.bookbla.americano.domain.quiz.repository.entity.QuizQuestion;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberBookReadResponse {

    private final String title;
    private final List<String> authors;
    private final String imageUrl;
    private final String review;
    private final String quiz;
    private final String firstChoice;
    private final String secondChoice;
    private final String thirdChoice;

    public static MemberBookReadResponse of(MemberBook memberBook, QuizQuestion quizQuestion) {
        Book book = memberBook.getBook();
        return new MemberBookReadResponse(
                book.getTitle(),
                book.getAuthors(),
                book.getImageUrl(),
                memberBook.getReview(),
                quizQuestion.getContents(),
                quizQuestion.getFirstChoice(),
                quizQuestion.getSecondChoice(),
                quizQuestion.getThirdChoice()
        );
    }
}
