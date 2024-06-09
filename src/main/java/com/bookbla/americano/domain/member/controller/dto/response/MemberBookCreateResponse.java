package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import com.bookbla.americano.domain.quiz.repository.entity.QuizQuestion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberBookCreateResponse {

    private final Long memberBookId;
    private final Long quizQuestionId;

    public static MemberBookCreateResponse from(MemberBook memberBook, QuizQuestion quizQuestion) {
        return MemberBookCreateResponse.builder()
                .memberBookId(memberBook.getId())
                .quizQuestionId(quizQuestion.getId())
                .build();
    }
}
