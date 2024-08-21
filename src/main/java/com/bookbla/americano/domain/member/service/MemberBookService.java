package com.bookbla.americano.domain.member.service;

import java.util.List;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.book.infra.aladin.AladinBookClient;
import com.bookbla.americano.domain.book.repository.BookRepository;
import com.bookbla.americano.domain.book.repository.entity.Book;
import com.bookbla.americano.domain.member.controller.dto.request.MemberBookCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberBookQuizUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberBookReviewUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberBookUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBookCreateResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBookReadResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBookReadResponses;
import com.bookbla.americano.domain.member.exception.MemberBookExceptionType;
import com.bookbla.americano.domain.member.repository.MemberBookRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.MemberStatusLogRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import com.bookbla.americano.domain.quiz.exception.QuizQuestionExceptionType;
import com.bookbla.americano.domain.quiz.repository.QuizQuestionRepository;
import com.bookbla.americano.domain.quiz.repository.entity.QuizQuestion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.bookbla.americano.domain.member.repository.entity.MemberBook.MEMBER_BOOK_REMOVABLE_COUNT;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberBookService {

    private final AladinBookClient aladinBookClient;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final MemberStatusLogRepository memberStatusLogRepository;
    private final MemberBookRepository memberBookRepository;
    private final QuizQuestionRepository quizQuestionRepository;

    public MemberBookCreateResponse addMemberBook(Long memberId, MemberBookCreateRequest request) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        Book book = bookRepository.findByIsbn(request.getIsbn())
                .orElseGet(() -> bookRepository.save(request.toBook()));

        validateMemberBookExists(member, book);

        aladinBookClient.findImageByIsbn13(book.getIsbn())
                .ifPresent(book::updateImageUrl);

        MemberBook savedMemberBook = memberBookRepository.save(request.toMemberBook(book, member));
        QuizQuestion savedQuizQuestion = quizQuestionRepository.save(request.toQuizQuestion(savedMemberBook));

        return MemberBookCreateResponse.from(savedMemberBook, savedQuizQuestion);
    }

    private void validateMemberBookExists(Member member, Book book) {
        if (memberBookRepository.existsByMemberAndBook(member, book)) {
            throw new BaseException(MemberBookExceptionType.MEMBER_BOOK_EXISTS);
        }
    }


    @Transactional(readOnly = true)
    public MemberBookReadResponses readMemberBooks(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        List<MemberBook> memberBooks = memberBookRepository.findByMemberOrderByCreatedAt(member);
        return MemberBookReadResponses.from(memberBooks);
    }

    @Transactional(readOnly = true)
    public MemberBookReadResponse readMemberBook(Long memberId, Long memberBookId) {
        MemberBook memberBook = memberBookRepository.getByIdOrThrow(memberBookId);
        QuizQuestion quizQuestion = quizQuestionRepository.findByMemberBook(memberBook)
                .orElseThrow(() -> new BaseException(
                        QuizQuestionExceptionType.MEMBER_QUIZ_QUESTION_NOT_FOUND));

        return MemberBookReadResponse.of(memberBook, quizQuestion);
    }

    public void updateMemberBook(
            MemberBookUpdateRequest request,
            Long memberBookId, Long memberId
    ) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberBook memberBook = memberBookRepository.getByIdOrThrow(memberBookId);

        memberBook.validateOwner(member);

        QuizQuestion quizQuestion = quizQuestionRepository.findByMemberBook(memberBook)
                .orElseThrow(() -> new BaseException(
                        QuizQuestionExceptionType.MEMBER_QUIZ_QUESTION_NOT_FOUND));

        memberBook.updateReview(request.getContents());
        quizQuestion.updateContents(request.getQuiz())
                .updateCorrectAnswer(request.getQuizAnswer())
                .updateFirstWrongAnswer(request.getFirstWrongChoice())
                .updateSecondWrongAnswer(request.getSecondWrongChoice());
    }

    public void updateMemberBookReview(MemberBookReviewUpdateRequest request, Long memberBookId,
                                       Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberBook memberBook = memberBookRepository.getByIdOrThrow(memberBookId);

        memberBook.validateOwner(member);

        memberBook.updateReview(request.getContents());
    }

    public void updateMemberBookQuiz(MemberBookQuizUpdateRequest request, Long memberBookId,
                                     Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberBook memberBook = memberBookRepository.getByIdOrThrow(memberBookId);

        memberBook.validateOwner(member);

        QuizQuestion quizQuestion = quizQuestionRepository.findByMemberBook(memberBook)
                .orElseThrow(() -> new BaseException(
                        QuizQuestionExceptionType.MEMBER_QUIZ_QUESTION_NOT_FOUND));

        quizQuestion.updateContents(request.getQuiz())
                .updateCorrectAnswer(request.getQuizAnswer())
                .updateFirstWrongAnswer(request.getFirstWrongChoice())
                .updateSecondWrongAnswer(request.getSecondWrongChoice());
    }

    public void deleteMemberBook(Long memberId, Long memberBookId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberBook memberBook = memberBookRepository.getByIdOrThrow(memberBookId);

        validateDeleteMemberBook(memberBook, member);

        memberBookRepository.deleteById(memberBookId);
    }

    private void validateDeleteMemberBook(MemberBook memberBook, Member member) {
        memberBook.validateOwner(member);
        long memberBookCounts = memberBookRepository.countByMember(member);
        if (memberBookCounts < MEMBER_BOOK_REMOVABLE_COUNT) {
            throw new BaseException(MemberBookExceptionType.MIN_MEMBER_REMOVABLE_BOOK_COUNT);
        }
    }
}
