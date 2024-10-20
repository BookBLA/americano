package com.bookbla.americano.domain.quiz.repository.entity;

import com.bookbla.americano.base.entity.BaseEntity;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import com.bookbla.americano.domain.quiz.enums.AnswerChoice;
import com.bookbla.americano.domain.quiz.enums.CorrectStatus;

import javax.persistence.*;

import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = @Index(name = "FK_MemberBook", columnList = "member_book_id"))
public class QuizQuestion extends BaseEntity {

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

    public CorrectStatus solve(String answer) {
        return answer.equals(firstChoice)
                ? CorrectStatus.CORRECT : CorrectStatus.WRONG;
    }
}
