package com.bookbla.americano.domain.postcard.service.dto.request;

import com.bookbla.americano.domain.quiz.repository.entity.QuizReply;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class SendPostcardRequest {
    private List<QuizAnswer> quizAnswerList;

    private Long postcardTypeId;
    private String imageUrl;

    private Long memberAskId;

    @NotBlank
    @Size(max = 150)
    private String memberReply;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QuizAnswer {
        private Long quizId;
        private String quizAnswer;
    }
}