package com.bookbla.americano.domain.member.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.controller.dto.request.MailVerifyRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberAuthUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberAuthResponse;
import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.bookbla.americano.domain.member.enums.MemberType;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberAuth;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import com.bookbla.americano.domain.member.service.dto.MemberAuthDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static com.bookbla.americano.domain.member.enums.EmailVerifyStatus.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
class MemberAuthServiceTest {

    @Autowired
    private MemberAuthService memberAuthService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 메일_인증시_사용한_메일이_다른_사용자가_등록한_이메일이라면_예외가_발생한다() {
        // given
        memberRepository.save(Member.builder()
                .memberProfile(MemberProfile.builder().build())
                .memberAuth(MemberAuth.builder().schoolEmail("학교이메일").build())
                .build());

        Member newMember = Member.builder()
                .memberProfile(MemberProfile.builder().build())
                .build();
        Member savedNewMember = memberRepository.save(newMember);

        // when, then
        assertThatThrownBy(() -> memberAuthService.sendEmailAndCreateMemberAuth(savedNewMember.getId(), new MemberAuthDto("학교이메일")))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("이메일이 이미 존재합니다.");
    }

    @Test
    void 사용자가_입력한_인증코드가_등록된_인증코드와_다르면_예외가_발생한다() {
        // given
        Member member = memberRepository.save(Member.builder()
                .memberProfile(MemberProfile.builder()
                                .build())
                .memberAuth(MemberAuth.builder()
                        .schoolEmail("학교이메일")
                        .emailVerifyStartTime(LocalDateTime.now())
                        .emailVerifyStatus(PENDING)
                        .emailVerifyCode("12345")
                        .build())
                .build());

        // when, then
        assertThatThrownBy(() -> memberAuthService.verifyEmail(member.getId(), new MailVerifyRequest("wrongCode")))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("인증코드가 다릅니다.");
    }

    @Test
    void 인증코드가_만료된_경우_예외가_발생한다() {
        // given
        Member member = memberRepository.save(Member.builder()
                .memberProfile(MemberProfile.builder()
                        .build())
                .memberAuth(MemberAuth.builder()
                        .schoolEmail("학교이메일")
                        .emailVerifyStartTime(LocalDateTime.now().plusMinutes(10))
                        .emailVerifyStatus(PENDING)
                        .emailVerifyCode("12345")
                        .build())
                .build());

        // when, then
        assertThatThrownBy(() -> memberAuthService.verifyEmail(member.getId(), new MailVerifyRequest("12345")))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("인증 시간이 만료되었습니다.");
    }


    @Test
    void 회원_정보를_불러올_수_있다() {
        // given
        Member member = Member.builder()
                .oauthEmail("bookbla@bookbla.com")
                .memberStatus(MemberStatus.APPROVAL)
                .oauthProfileImageUrl("카카오프로필사진")
                .memberType(MemberType.KAKAO)
                .memberProfile(
                        MemberProfile.builder()
                                .schoolName("경찰대")
                                .major("도둑학과")
                                .name("장발장")
                                .nickname("대도")
                                .birthDate(LocalDate.ofYearDay(2020, 4))
                                .build()
                )
                .memberAuth(
                        MemberAuth.builder()
                                .emailVerifyCode("123456")
                                .schoolEmail("학교이메일")
                                .emailVerifyStatus(DONE)
                                .emailVerifyStartTime(LocalDateTime.now())
                                .build()
                )
                .build();
        Member savedMember = memberRepository.save(member);

        // when
        MemberAuthResponse memberAuthResponse = memberAuthService.readMemberAuth(savedMember.getId());

        // then
        assertAll(
                () -> assertThat(memberAuthResponse.getMemberId()).isNotNull(),
                () -> assertThat(memberAuthResponse.getSchoolEmail()).isEqualTo("학교이메일"),
                () -> assertThat(memberAuthResponse.getEmailVerifyStatus()).isEqualToIgnoringCase("done")
        );
    }

    @Test
    void 회원의_인증_이메일_정보를_업데이트_할_수_있다() {
        // given
        Member member = Member.builder()
                .oauthEmail("bookbla@bookbla.com")
                .memberStatus(MemberStatus.APPROVAL)
                .oauthProfileImageUrl("카카오프로필사진")
                .memberType(MemberType.KAKAO)
                .memberProfile(
                        MemberProfile.builder()
                                .schoolName("경찰대")
                                .major("도둑학과")
                                .name("장발장")
                                .nickname("대도")
                                .birthDate(LocalDate.ofYearDay(2020, 4))
                                .build()
                )
                .memberAuth(
                        MemberAuth.builder()
                                .emailVerifyCode("123456")
                                .schoolEmail("학교이메일")
                                .emailVerifyStatus(DONE)
                                .emailVerifyStartTime(LocalDateTime.now())
                                .build()
                )
                .build();
        Member savedMember = memberRepository.save(member);

        // when
        memberAuthService.updateMemberAuth(savedMember.getId(), new MemberAuthUpdateRequest("바뀐이메일"));

        // then
        MemberAuthResponse memberAuthResponse = memberAuthService.readMemberAuth(savedMember.getId());
        assertAll(
                () -> assertThat(memberAuthResponse.getMemberId()).isNotNull(),
                () -> assertThat(memberAuthResponse.getSchoolEmail()).isEqualTo("바뀐이메일"),
                () -> assertThat(memberAuthResponse.getEmailVerifyStatus()).isEqualToIgnoringCase("done")
        );
    }

    @AfterEach
    void tearDown() {
        memberRepository.deleteAll();
    }
}
