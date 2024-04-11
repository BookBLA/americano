package com.bookbla.americano.domain.member.service.impl;

import java.util.List;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.book.repository.BookRepository;
import com.bookbla.americano.domain.book.repository.entity.Book;
import com.bookbla.americano.domain.member.controller.dto.request.MemberBookCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberBookUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBookCreateResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBookReadResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBookReadResponses;
import com.bookbla.americano.domain.member.exception.MemberBookExceptionType;
import com.bookbla.americano.domain.member.repository.MemberBookRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import com.bookbla.americano.domain.member.service.MemberBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.bookbla.americano.domain.member.repository.entity.MemberBook.MAX_MEMBER_BOOK_COUNT;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberBookServiceImpl implements MemberBookService {

    private static final int OLD_MEMBER_BOOK = 0;

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final MemberBookRepository memberBookRepository;

    @Override
    public MemberBookCreateResponse addMemberBook(Long memberId, MemberBookCreateRequest memberBookCreateRequest) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        Book book = bookRepository.findByIsbn(memberBookCreateRequest.getIsbn())
                .orElseGet(() -> bookRepository.save(memberBookCreateRequest.toBook()));

        validateAddMemberBook(member, book);

        MemberBook memberBook = MemberBook.builder()
                .book(book)
                .isRepresentative(memberBookCreateRequest.getIsRepresentative())
                .member(member)
                .build();
        MemberBook savedMemberBook = memberBookRepository.save(memberBook);
        return MemberBookCreateResponse.from(savedMemberBook);
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

    @Override
    @Transactional(readOnly = true)
    public MemberBookReadResponses readMemberBooks(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        List<MemberBook> memberBooks = memberBookRepository.findByMemberOrderByCreatedAt(member);
        return MemberBookReadResponses.from(memberBooks);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberBookReadResponse readMemberBook(Long memberBookId) {
        MemberBook memberBook = memberBookRepository.getByIdOrThrow(memberBookId);
        return MemberBookReadResponse.of(memberBook);
    }

    @Override
    public void updateMemberBook(
            MemberBookUpdateRequest memberBookUpdateRequest,
            Long memberBookId, Long memberId
    ) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberBook memberBook = memberBookRepository.getByIdOrThrow(memberBookId);

        memberBook.validateOwner(member);

        memberBook.updateReview(memberBookUpdateRequest.getContents());
    }


    @Override
    public void deleteMemberBook(Long memberId, Long memberBookId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberBook memberBook = memberBookRepository.getByIdOrThrow(memberBookId);

        memberBook.validateOwner(member);

        if (memberBook.isNotRepresentative()) {
            memberBookRepository.deleteById(memberBookId);
            return;
        }

        memberBookRepository.deleteById(memberBookId);
        List<MemberBook> memberBooks = memberBookRepository.findByMemberOrderByCreatedAt(member);
        if (!memberBooks.isEmpty()) {
            memberBooks.get(OLD_MEMBER_BOOK).updateRepresentative();
        }
    }
}
