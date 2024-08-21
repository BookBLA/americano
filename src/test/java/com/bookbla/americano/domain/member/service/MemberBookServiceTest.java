package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.book.repository.BookRepository;
import com.bookbla.americano.domain.book.repository.entity.Book;
import com.bookbla.americano.domain.member.repository.MemberBookRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
class MemberBookServiceTest {

    @Autowired
    private MemberBookService memberBookService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberBookRepository memberBookRepository;

    @Test
    void 회원의_책이_한_권_이하라면_책_삭제시_예외가_발생한다() {
        // given
        Book book = bookRepository.save(Book.builder().isbn("1").title("오브젝트").build());
        Member member = memberRepository.save(Member.builder().memberProfile(MemberProfile.builder().name("이준희").build()).build());
        MemberBook memberBook = memberBookRepository.save(MemberBook.builder()
                .book(book)
                .member(member)
                .build());

        // when, then
        assertThatThrownBy(() -> memberBookService.deleteMemberBook(member.getId(), memberBook.getId()))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("도서는 2권 이상 등록 후 삭제할 수 있습니다.");
    }

    @AfterEach
    void tearDown() {
        bookRepository.deleteAll();
        memberRepository.deleteAll();
        memberBookRepository.deleteAll();
    }
}
