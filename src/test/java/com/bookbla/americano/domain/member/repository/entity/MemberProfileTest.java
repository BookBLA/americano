package com.bookbla.americano.domain.member.repository.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bookbla.americano.base.exception.BaseException;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberProfileTest {

    @Test
    void 한국_나이를_계산할_수_있다() {
        // given
        LocalDate baseDate = LocalDate.of(2024, 3, 1);
        LocalDate birthDate = LocalDate.of(2005, 3, 1);
        MemberProfile memberProfile = MemberProfile.builder()
                .birthDate(birthDate)
                .build();

        // when
        int age = memberProfile.calculateAge(baseDate);

        // then
        assertThat(age).isEqualTo(20);
    }

    @Test
    void 비속어_입력_시_닉네임_수정이_불가능_해야한다() {
        //given
        MemberProfile memberProfile = MemberProfile.builder()
                .name("고도도")
                .build();

        //when then
        assertThatThrownBy(() -> memberProfile.updateName("좃만이"))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("사용할 수 없는 단어가(비속어, 북블라 등) 포함되어있습니다.");
    }

    @Test
    void 북블라_입력_시_닉네임_수정이_불가능_해야한다() {
        //given
        MemberProfile memberProfile = MemberProfile.builder()
                .name("고도도")
                .build();

        //when then
        assertThatThrownBy(() -> memberProfile.updateName("북블라사랑해"))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("사용할 수 없는 단어가(비속어, 북블라 등) 포함되어있습니다.");
    }
}
