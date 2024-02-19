package com.bookbla.americano.domain.memberask.repository.entity;

import static org.assertj.core.api.Assertions.*;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.Member;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberAskTest {

    @Test
    void 개인_질문의_제한_글자수를_초과하면_예외가_발생한다() {
        // given
        Member member = Member.builder().build();
        String content = " ".repeat(81);

        // when, then
        assertThatThrownBy(() -> MemberAsk.builder().member(member).contents(content).build())
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("개인 질문은 공백 포함 80자 이하로만 작성가능합니다");
    }

}
