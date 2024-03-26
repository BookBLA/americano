package com.bookbla.americano.domain.admin.repository.entity;

import com.bookbla.americano.base.exception.BaseException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AdminTest {

    @Test
    void 인증_카운트_회수를_초과하면_예외가_발생한다() {
        // given
        int invalidFailCount = 5;

        Admin admin = Admin.builder()
                .userId("bookbla")
                .failCount(invalidFailCount)
                .password("1234")
                .build();

        // when, then
        assertThatThrownBy(admin::validateFailCount)
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("최대 비밀번호 입력 횟수를 초과했습니다");
    }

    @Test
    void 인증_실패_카운트_횟수를_증가시킨다() {
        // given
        Admin admin = Admin.builder()
                .userId("bookbla")
                .password("1234")
                .build();

        // when
        admin.increaseFailCount();

        // then
        assertThat(admin.getFailCount()).isOne();
    }

    @Test
    void 인증_실패_카운트_횟수를_초기화시킨다() {
        // given
        Admin admin = Admin.builder()
                .userId("bookbla")
                .password("1234")
                .build();
        admin.increaseFailCount();

        // when
        admin.resetFailCount();

        // then
        assertThat(admin.getFailCount()).isZero();
    }

}
