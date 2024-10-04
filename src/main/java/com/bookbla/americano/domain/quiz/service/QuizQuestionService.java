package com.bookbla.americano.domain.quiz.service;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.matching.exception.MemberMatchingExceptionType;
import com.bookbla.americano.domain.matching.repository.MatchExcludedRepository;
import com.bookbla.americano.domain.matching.repository.MemberMatchingRepository;
import com.bookbla.americano.domain.matching.repository.entity.MatchExcludedInfo;
import com.bookbla.americano.domain.matching.repository.entity.MemberMatching;
import com.bookbla.americano.domain.member.repository.MemberBookRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import com.bookbla.americano.domain.quiz.controller.dto.request.QuizQuestionVerifyRequest;
import com.bookbla.americano.domain.quiz.controller.dto.response.QuizQuestionReadResponse;
import com.bookbla.americano.domain.quiz.controller.dto.response.QuizQuestionVerifyResponse;
import com.bookbla.americano.domain.quiz.enums.CorrectStatus;
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
    private final MatchExcludedRepository matchExcludedRepository;
    private final MemberMatchingRepository memberMatchingRepository;

    @Transactional(readOnly = true)
    public QuizQuestionReadResponse getQuizQuestion(Long memberId, Long memberBookId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberBook memberBook = memberBookRepository.getByIdOrThrow(memberBookId);

        QuizQuestion quizQuestion = quizQuestionRepository.findByMemberBook(memberBook)
                .orElseThrow(() -> new BaseException(QuizQuestionExceptionType.MEMBER_QUIZ_QUESTION_NOT_FOUND));

        if (!memberBook.isOwner(member)) {
            return QuizQuestionReadResponse.fromShuffledChoices(quizQuestion, memberBook);
        }
        return QuizQuestionReadResponse.from(quizQuestion, memberBook);
    }

    public QuizQuestionVerifyResponse verifyQuizQuestion(Long memberId, QuizQuestionVerifyRequest request) {
        QuizQuestion quizQuestion = quizQuestionRepository.findById(request.getQuizId())
                .orElseThrow(() -> new BaseException(QuizQuestionExceptionType.MEMBER_QUIZ_QUESTION_NOT_FOUND));

        CorrectStatus status = quizQuestion.solve(request.getQuizAnswer());
        MemberMatching memberMatching = memberMatchingRepository.findByMemberId(memberId)
                .orElseThrow(() -> new BaseException(MemberMatchingExceptionType.NOT_FOUND_MATCHING));

        boolean isCorrect = true;

        if (status == CorrectStatus.WRONG) {
            matchExcludedRepository.findByMemberIdAndExcludedMemberId(request.getQuizMakerId(), memberId)
                    .orElseGet(() -> matchExcludedRepository.save(MatchExcludedInfo.of(memberId, request.getQuizMakerId())));
            memberMatching.updateInvitationCard(true);
            isCorrect = false;
        }

        return QuizQuestionVerifyResponse.of(isCorrect);
    }
}
