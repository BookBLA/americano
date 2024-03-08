package com.bookbla.americano.domain.quiz.service.impl;

import com.bookbla.americano.domain.member.repository.MemberBookRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import com.bookbla.americano.domain.quiz.QuizQuestion;
import com.bookbla.americano.domain.quiz.controller.dto.request.QuizQuestionCreateRequest;
import com.bookbla.americano.domain.quiz.controller.dto.response.QuizQuestionCreateResponse;
import com.bookbla.americano.domain.quiz.repository.QuizQuestionRepository;
import com.bookbla.americano.domain.quiz.service.QuizQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class QuizQuestionServiceImpl implements QuizQuestionService {

    private final QuizQuestionRepository quizQuestionRepository;
    private final MemberRepository memberRepository;
    private final MemberBookRepository memberBookRepository;

    @Override
    public QuizQuestionCreateResponse createQuizQuestion(Long memberId, Long memberBookId, QuizQuestionCreateRequest quizQuestionCreateRequest) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberBook memberBook = memberBookRepository.getByIdOrThrow(memberBookId);

        memberBook.validateOwner(member);

        memberBook.updateReview(quizQuestionCreateRequest.getReview());

        QuizQuestion quizQuestion = quizQuestionCreateRequest.toQuizQuestionWith(memberBook);
        return new QuizQuestionCreateResponse(quizQuestionRepository.save(quizQuestion).getId());
    }
}
