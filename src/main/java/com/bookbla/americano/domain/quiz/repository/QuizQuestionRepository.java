package com.bookbla.americano.domain.quiz.repository;

import com.bookbla.americano.domain.quiz.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {

}
