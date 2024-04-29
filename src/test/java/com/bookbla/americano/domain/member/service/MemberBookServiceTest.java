package com.bookbla.americano.domain.member.service;

import java.util.List;

import com.bookbla.americano.domain.book.repository.BookRepository;
import com.bookbla.americano.domain.book.repository.entity.Book;
import com.bookbla.americano.domain.member.repository.MemberBookRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import com.bookbla.americano.domain.member.service.MemberBookService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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
    void 회원의_대표책_삭제시_두번째로_등록된_책이_대표책이_된다() {
        // given
        Book firstBook = bookRepository.save(Book.builder().isbn("1").title("오브젝트").build());
        Book secondBook = bookRepository.save(Book.builder().isbn("2").title("DDD").build());
        Book thirdBook = bookRepository.save(Book.builder().isbn("3").title("클린아키텍처").build());

        Member member = memberRepository.save(Member.builder().memberProfile(MemberProfile.builder().name("이준희").build()).build());

        MemberBook memberBook = memberBookRepository.save(MemberBook.builder().book(firstBook).isRepresentative(true).member(member).build());
        memberBookRepository.save(MemberBook.builder().book(secondBook).member(member).build());
        memberBookRepository.save(MemberBook.builder().book(thirdBook).member(member).build());

        // when
        memberBookService.deleteMemberBook(member.getId(), memberBook.getId());

        // then
        List<MemberBook> memberBooks = memberBookRepository.findByMemberOrderByCreatedAt(member);
        assertAll(
                () -> assertThat(memberBooks).hasSize(2),
                () -> assertThat(memberBooks.get(0).isRepresentative()).isTrue(),
                () -> assertThat(memberBooks.get(1).isRepresentative()).isFalse()
        );
    }

    @AfterEach
    void tearDown() {
        bookRepository.deleteAll();
        memberRepository.deleteAll();
        memberBookRepository.deleteAll();
    }
}
