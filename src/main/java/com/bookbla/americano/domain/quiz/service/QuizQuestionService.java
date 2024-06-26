package com.bookbla.americano.domain.quiz.service;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.repository.MemberBookRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import com.bookbla.americano.domain.quiz.controller.dto.request.QuizQuestionCreateRequest;
import com.bookbla.americano.domain.quiz.controller.dto.request.QuizQuestionUpdateRequest;
import com.bookbla.americano.domain.quiz.controller.dto.response.QuizQuestionReadResponse;
import com.bookbla.americano.domain.quiz.exception.QuizQuestionExceptionType;
import com.bookbla.americano.domain.quiz.repository.QuizQuestionRepository;
import com.bookbla.americano.domain.quiz.repository.entity.QuizQuestion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class QuizQuestionService {

    private final QuizQuestionRepository quizQuestionRepository;
    private final MemberRepository memberRepository;
    private final MemberBookRepository memberBookRepository;

    public Long createQuizQuestion(
            Long memberId, Long memberBookId,
            QuizQuestionCreateRequest quizQuestionCreateRequest
    ) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberBook memberBook = memberBookRepository.getByIdOrThrow(memberBookId);

        memberBook.validateOwner(member);
        memberBook.updateReview(quizQuestionCreateRequest.getReview());

        QuizQuestion quizQuestion = quizQuestionCreateRequest.toQuizQuestionWith(memberBook);
        return quizQuestionRepository.save(quizQuestion).getId();
    }

    @Transactional(readOnly = true)
    public QuizQuestionReadResponse getQuizQuestion(Long memberId, Long memberBookId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberBook memberBook = memberBookRepository.getByIdOrThrow(memberBookId);

        QuizQuestion quizQuestion = quizQuestionRepository.findByMemberBook(memberBook)
                .orElseThrow(() -> new BaseException(
                        QuizQuestionExceptionType.MEMBER_QUIZ_QUESTION_NOT_FOUND));

        if (!memberBook.isOwner(member)) {
            return QuizQuestionReadResponse.fromShuffledChoices(quizQuestion, memberBook);
        }
        return QuizQuestionReadResponse.from(quizQuestion, memberBook);
    }

    public void updateQuizQuestion(
            Long memberId, Long memberBookId,
            QuizQuestionUpdateRequest quizQuestionUpdateRequest) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberBook memberBook = memberBookRepository.getByIdOrThrow(memberBookId);

        memberBook.validateOwner(member);
        QuizQuestion quizQuestion = quizQuestionRepository.findByMemberBook(memberBook)
                .orElseThrow(() -> new BaseException(QuizQuestionExceptionType.MEMBER_QUIZ_QUESTION_NOT_FOUND));

        memberBook.updateReview(quizQuestionUpdateRequest.getReview());
        update(quizQuestion, quizQuestionUpdateRequest);
    }

    private void update(QuizQuestion quizQuestion, QuizQuestionUpdateRequest request) {
        quizQuestion.updateContents(request.getQuiz())
                .updateCorrectAnswer(request.getQuizAnswer())
                .updateFirstWrongAnswer(request.getFirstWrongChoice())
                .updateSecondWrongAnswer(request.getSecondWrongChoice());
    }

}
