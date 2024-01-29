package com.bookbla.americano.domain.quiz;

import com.bookbla.americano.domain.member.MemberBook;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookQuizQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_book_id")
    private MemberBook memberBook;

    @Enumerated(EnumType.STRING)
    private QuizType quiztype;

    private String questionChoice;

    private String choice1;

    private String choice2;

    private String choice3;

    private String choice4;

    private String answerChoice;

    private String questionSubjective;

    private String answerSubjective;

}
