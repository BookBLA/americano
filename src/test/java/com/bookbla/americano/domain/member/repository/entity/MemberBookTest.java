package com.bookbla.americano.domain.member.repository.entity;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.bookbla.americano.base.exception.BaseException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberBookTest {

    @Test
    void 도서를_등록한_회원인지_확인할_수_있다() {
        // given
        Member member = Member.builder().id(1L).build();
        MemberBook memberBook = MemberBook.builder().member(member).build();

        // when, then
        assertDoesNotThrow(() -> memberBook.validateOwner(member));
    }

    @Test
    void 도서를_등록한_회원이_아니라면_예외를_반환한다() {
        // given
        Member jinho = Member.builder().id(1L).build();
        MemberBook jinhoBook = MemberBook.builder().member(jinho).build();

        Member junhee = Member.builder().id(2L).build();

        // when, then
        assertThatThrownBy(() -> jinhoBook.validateOwner(junhee))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("해당 도서를 등록한 회원이 아닙니다.");
    }

}
