package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.controller.dto.request.MemberInformationUpdateRequest;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.bookbla.americano.fixture.Fixture.스타일_등록_완료_남성_고도리;
import static com.bookbla.americano.fixture.Fixture.프로필_등록_완료_남성_리준희;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService sut;

    @Nested
    class 회원_정보_수정 {

        @Test
        void 다른_이가_사용중인_닉네임으로_변경할_수_없다() {
            // given
            memberRepository.save(프로필_등록_완료_남성_리준희);
            Member dohyeonKoh = memberRepository.save(스타일_등록_완료_남성_고도리);
            var request = new MemberInformationUpdateRequest("리준희", "istp", "가끔", 180);

            // when, then
            assertThatThrownBy(() -> sut.updateMemberInformation(dohyeonKoh.getId(), request))
                    .isInstanceOf(BaseException.class)
                    .hasMessageContaining("이미 사용중인 닉네임입니다.");
        }

        @Test
        void 본인이_사용중인_닉네임과_동일한_닉네임으로_변경하면_닉네임이_유지된다() {
            // given
            Member dohyeonKoh = memberRepository.save(스타일_등록_완료_남성_고도리);
            var request = new MemberInformationUpdateRequest("고도리", "istp", "가끔", 180);

            // when
            sut.updateMemberInformation(dohyeonKoh.getId(), request);

            // then
            assertThat(dohyeonKoh.getMemberProfile().getName()).isEqualTo("고도리");
        }

    }

    @AfterEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
    }
}
