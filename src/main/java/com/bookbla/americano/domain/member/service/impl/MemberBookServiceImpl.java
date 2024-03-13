package com.bookbla.americano.domain.member.service.impl;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.book.repository.entity.Book;
import com.bookbla.americano.domain.book.repository.BookRepository;
import com.bookbla.americano.domain.member.controller.dto.request.MemberBookCreateRequest;
import com.bookbla.americano.domain.member.exception.MemberBookExceptionType;
import com.bookbla.americano.domain.member.repository.MemberBookRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import com.bookbla.americano.domain.member.service.MemberBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberBookServiceImpl implements MemberBookService {

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final MemberBookRepository memberBookRepository;

    @Override
    public Long addMemberBook(Long memberId, MemberBookCreateRequest memberBookCreateRequest) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        Book book = bookRepository.findByIsbn(memberBookCreateRequest.getIsbn())
                .orElseGet(() -> bookRepository.save(memberBookCreateRequest.toBook()));

        if (memberBookRepository.existsByMemberAndBook(member, book)) {
            throw new BaseException(MemberBookExceptionType.MEMBER_BOOK_EXISTS);
        }

        MemberBook memberBook = MemberBook.builder()
                .book(book)
                .isRepresentative(memberBookCreateRequest.getIsRepresentative())
                .member(member)
                .build();
        return memberBookRepository.save(memberBook).getId();
    }
}
