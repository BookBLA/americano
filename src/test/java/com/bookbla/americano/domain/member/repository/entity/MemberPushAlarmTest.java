package com.bookbla.americano.domain.member.repository.entity;

import com.bookbla.americano.base.exception.BaseException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberPushAlarmTest {

    @Test
    void 알림의_소유자가_아니라면_예외를_반환한다() {
        // given
        Member member = Member.builder().id(1L).build();
        MemberPushAlarm memberPushAlarm = MemberPushAlarm.builder()
                .member(member)
                .title("제목")
                .body("내용")
                .build();

        // when, then
        assertThatThrownBy(() -> memberPushAlarm.validateOwner(-1L))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("해당 알림의 소유자가 아닙니다");
    }

    @Test
    void 알림의_소유자인지_검증한다() {
        // given
        Member member = Member.builder().id(1L).build();
        MemberPushAlarm memberPushAlarm = MemberPushAlarm.builder()
                .member(member)
                .title("제목")
                .body("내용")
                .build();

        // when, then
        assertDoesNotThrow(() -> memberPushAlarm.validateOwner(member.getId()));
    }
}
