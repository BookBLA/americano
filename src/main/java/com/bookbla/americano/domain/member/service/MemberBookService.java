package com.bookbla.americano.domain.member.service;

import java.time.LocalDateTime;
import java.util.List;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.book.repository.BookRepository;
import com.bookbla.americano.domain.book.repository.entity.Book;
import com.bookbla.americano.domain.member.controller.dto.request.MemberBookCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberBookQuizUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberBookReviewUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberBookUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBookCreateResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBookReadResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBookReadResponses;
import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.bookbla.americano.domain.member.exception.MemberBookExceptionType;
import com.bookbla.americano.domain.member.repository.MemberBookRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.MemberStatusLogRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import com.bookbla.americano.domain.member.repository.entity.MemberStatusLog;
import com.bookbla.americano.domain.quiz.exception.QuizQuestionExceptionType;
import com.bookbla.americano.domain.quiz.repository.QuizQuestionRepository;
import com.bookbla.americano.domain.quiz.repository.entity.QuizQuestion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.bookbla.americano.domain.member.enums.MemberStatus.COMPLETED;
import static com.bookbla.americano.domain.member.repository.entity.MemberBook.MAX_MEMBER_BOOK_COUNT;
import static com.bookbla.americano.domain.member.repository.entity.MemberBook.MEMBER_BOOK_REMOVABLE_COUNT;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberBookService {

    private static final int OLD_MEMBER_BOOK = 0;

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final MemberStatusLogRepository memberStatusLogRepository;
    private final MemberBookRepository memberBookRepository;
    private final QuizQuestionRepository quizQuestionRepository;

    public MemberBookCreateResponse addMemberBook(Long memberId, MemberBookCreateRequest request) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        Book book = bookRepository.findByIsbn(request.getIsbn())
                .orElseGet(() -> bookRepository.save(request.toBook()));

        validateAddMemberBook(member, book);

        MemberBook savedMemberBook = memberBookRepository.save(request.toMemberBook(book, member));
        QuizQuestion savedQuizQuestion = quizQuestionRepository.save(
                request.toQuizQuestion(savedMemberBook));

        if (request.getIsRepresentative()) {
            memberStatusLogRepository.save(
                    MemberStatusLog.builder()
                            .memberId(member.getId())
                            .beforeStatus(member.getMemberStatus())
                            .afterStatus(MemberStatus.COMPLETED)
                            .build()
            );
            member.updateMemberStatus(COMPLETED, LocalDateTime.now());
        }
        return MemberBookCreateResponse.from(savedMemberBook, savedQuizQuestion);
    }

    private void validateAddMemberBook(Member member, Book book) {
        if (memberBookRepository.existsByMemberAndBook(member, book)) {
            throw new BaseException(MemberBookExceptionType.MEMBER_BOOK_EXISTS);
        }
        long memberBookCounts = memberBookRepository.countByMember(member);
        if (memberBookCounts >= MAX_MEMBER_BOOK_COUNT) {
            throw new BaseException(MemberBookExceptionType.MAX_MEMBER_BOOK_COUNT);
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

        if (memberBook.isNotRepresentative()) {
            memberBookRepository.deleteById(memberBookId);
            return;
        }
        deleteRepresentativeBook(memberBookId, member);
    }

    private void validateDeleteMemberBook(MemberBook memberBook, Member member) {
        memberBook.validateOwner(member);
        long memberBookCounts = memberBookRepository.countByMember(member);
        if (memberBookCounts < MEMBER_BOOK_REMOVABLE_COUNT) {
            throw new BaseException(MemberBookExceptionType.MIN_MEMBER_REMOVABLE_BOOK_COUNT);
        }
    }

    private void deleteRepresentativeBook(Long memberBookId, Member member) {
        memberBookRepository.deleteById(memberBookId);
        List<MemberBook> memberBooks = memberBookRepository.findByMemberOrderByCreatedAt(member);
        if (!memberBooks.isEmpty()) {
            memberBooks.get(OLD_MEMBER_BOOK).updateRepresentative();
        }
    }
}
