package com.bookbla.americano.domain.quiz;

import com.bookbla.americano.base.entity.BaseInsertEntity;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizQuestion extends BaseInsertEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_book_id")
    private MemberBook memberBook;

    private String contents;

    private String firstChoice;

    private String secondChoice;

    private String thirdChoice;

    // 첫번째 답변을 정답으로 저장
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private AnswerChoice answerChoice = AnswerChoice.FIRST;

    public QuizQuestion updateContents(String contents) {
        this.contents = contents;
        return this;
    }

    public QuizQuestion updateCorrectAnswer(String answer) {
        this.firstChoice = answer;
        return this;
    }

    public QuizQuestion updateFirstWrongAnswer(String firstWrongAnswer) {
        this.secondChoice = firstWrongAnswer;
        return this;
    }

    public QuizQuestion updateSecondWrongAnswer(String secondWrongAnswer) {
        this.thirdChoice = secondWrongAnswer;
        return this;
    }

    public QuizQuestion shuffleChoices(QuizQuestion quizQuestion) {
        // 선택지를 리스트로 변환
        List<String> choices = new ArrayList<>(Arrays.asList(
                quizQuestion.getFirstChoice(),
                quizQuestion.getSecondChoice(),
                quizQuestion.getThirdChoice()
        ));

        // 선택지 리스트를 랜덤하게 섞음
        Collections.shuffle(choices);

        // 랜덤하게 섞인 선택지를 다시 QuizQuestion 객체에 설정
        quizQuestion.setFirstChoice(choices.get(0));
        quizQuestion.setSecondChoice(choices.get(1));
        quizQuestion.setThirdChoice(choices.get(2));

        return quizQuestion;
    }
}
