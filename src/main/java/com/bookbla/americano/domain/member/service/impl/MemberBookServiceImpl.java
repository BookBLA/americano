package com.bookbla.americano.domain.member.service.impl;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.book.repository.entity.Book;
import com.bookbla.americano.domain.book.repository.BookRepository;
import com.bookbla.americano.domain.member.controller.dto.request.MemberBookCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBookReadResponses;
import com.bookbla.americano.domain.member.exception.MemberBookExceptionType;
import com.bookbla.americano.domain.member.exception.MemberProfileExceptionType;
import com.bookbla.americano.domain.member.repository.MemberBookRepository;
import com.bookbla.americano.domain.member.repository.MemberProfileRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import com.bookbla.americano.domain.member.service.MemberBookService;
import java.util.List;
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
    private final MemberProfileRepository memberProfileRepository;

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

    @Override
    @Transactional(readOnly = true)
    public MemberBookReadResponses readMemberBooks(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);

        memberProfileRepository.findByMember(member)
                .orElseThrow(() -> new BaseException(MemberProfileExceptionType.PROFILE_NOT_FOUND));

        List<MemberBook> memberBooks = memberBookRepository.findByMember(member);
        return MemberBookReadResponses.from(memberBooks);
    }

    @Override
    public void deleteMemberBook(Long memberId, Long memberBookId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberBook memberBook = memberBookRepository.getByIdOrThrow(memberBookId);

        memberBook.validateOwner(member);

        memberBookRepository.deleteById(memberBookId);
    }

}
