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

@Entity
@Getter
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
}
