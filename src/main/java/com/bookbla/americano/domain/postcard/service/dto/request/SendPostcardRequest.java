package com.bookbla.americano.domain.postcard.service.dto.request;

import com.bookbla.americano.domain.quiz.repository.entity.QuizReply;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class SendPostcardRequest {

    @NotNull(message = "퀴즈가 입력되지 않았습니다.")
    private List<QuizAnswer> quizAnswerList;

    @NotNull(message = "postcardTypeId가 입력되지 않았습니다.")
    private Long postcardTypeId;

    @NotNull(message = "imageUrl이 입력되지 않았습니다.")
    private String imageUrl;

    @NotNull(message = "memberAskId가 입력되지 않았습니다.")
    private Long memberAskId;

    @NotNull(message = "memberReply가 입력되지 않았습니다.")
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