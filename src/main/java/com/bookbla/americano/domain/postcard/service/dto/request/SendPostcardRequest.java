package com.bookbla.americano.domain.postcard.service.dto.request;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class SendPostcardRequest {

    @NotNull(message = "퀴즈가 입력되지 않았습니다.")
    private QuizAnswer quizAnswer;

    @NotNull(message = "postcardTypeId가 입력되지 않았습니다.")
    private Long postcardTypeId;

    @NotNull(message = "엽서를 보낼 상대방의 식별자가 입력되지 않았습니다")
    private Long receiveMemberId;

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