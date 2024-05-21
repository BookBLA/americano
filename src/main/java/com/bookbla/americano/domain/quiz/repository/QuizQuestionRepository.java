package com.bookbla.americano.domain.quiz.repository;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import com.bookbla.americano.domain.quiz.exception.QuizQuestionExceptionType;
import com.bookbla.americano.domain.quiz.repository.entity.QuizQuestion;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {

    default QuizQuestion getByIdOrThrow(Long quizQuestionId) {
        return findById(quizQuestionId)
                .orElseThrow(() -> new BaseException(QuizQuestionExceptionType.MEMBER_QUIZ_QUESTION_NOT_FOUND));
    }

    Optional<QuizQuestion> findByMemberBook(MemberBook memberBook);

    void deleteByMemberBook(MemberBook memberBook);
}
