package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.controller.dto.request.MemberStyleCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberStyleUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberStyleResponse;
import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.bookbla.americano.domain.member.enums.MemberType;
import com.bookbla.americano.domain.member.enums.SmokeType;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.ProfileImageTypeRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberStyle;
import com.bookbla.americano.domain.member.repository.entity.ProfileImageType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.bookbla.americano.domain.member.enums.Mbti.INFJ;
import static com.bookbla.americano.domain.member.enums.Mbti.INTP;
import static com.bookbla.americano.domain.member.enums.SmokeType.SMOKE;
import static com.bookbla.americano.domain.member.enums.SmokeType.SOMETIMES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberStyleServiceTest {

    @Autowired
    private MemberStyleService memberStyleService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProfileImageTypeRepository profileImageTypeRepository;

    private ProfileImageType profileImageType;

    @BeforeEach
    void setUp() {
        profileImageType = profileImageTypeRepository.save(ProfileImageType.builder().build());
    }

    @Test
    void 회원의_스타일을_생성할_수_있다() {
        // given
        Member member = memberRepository.save(Member.builder()
                .memberType(MemberType.APPLE)
                .oauthEmail("bookbla@bookbla.com")
                .build());
        MemberStyleCreateRequest memberStyleCreateRequest = new MemberStyleCreateRequest(
                "infj", "가끔", 160, profileImageType.getId()
        );

        // when
        MemberStyleResponse memberStyleResponse = memberStyleService.createMemberStyle(
                member.getId(),
                memberStyleCreateRequest);

        // then
        assertAll(
                () -> assertThat(memberStyleResponse.getMemberId()).isNotNull(),
                () -> assertThat(memberStyleResponse.getMbti()).isEqualToIgnoringCase(INFJ.name()),
                () -> assertThat(memberStyleResponse.getSmokeType()).isEqualTo(SmokeType.SOMETIMES.getDetailValue()),
                () -> assertThat(memberStyleResponse.getHeight()).isEqualTo(160),
                () -> assertThat(memberStyleResponse.getProfileImageTypeId()).isEqualTo(profileImageType.getId())
        );
    }

    @Test
    void 회원의_스타일을_조회할_수_있다() {
        // given
        Member member = memberRepository.save(Member.builder()
                .memberType(MemberType.APPLE)
                .oauthEmail("bookbla@bookbla.com")
                .memberStatus(MemberStatus.COMPLETED)
                .memberStyle(MemberStyle.builder()
                        .smokeType(SMOKE)
                        .mbti(INTP)
                        .height(160)
                        .profileImageType(profileImageType)
                        .build())
                .build());

        // when
        MemberStyleResponse memberStyleResponse = memberStyleService.readMemberStyle(
                member.getId());

        // then
        assertAll(
                () -> assertThat(memberStyleResponse.getMemberId()).isNotNull(),
                () -> assertThat(memberStyleResponse.getSmokeType()).isEqualTo(
                        SMOKE.getDetailValue()),
                () -> assertThat(memberStyleResponse.getMbti()).isEqualToIgnoringCase(INTP.name()),
                () -> assertThat(memberStyleResponse.getHeight()).isEqualTo(160)
        );
    }

    @Test
    void 스타일이_등록되지_않은_회원은_스타일_조회시_예외가_발생한다() {
        // given
        Member member = memberRepository.save(Member.builder()
                .memberType(MemberType.APPLE)
                .oauthEmail("bookbla@bookbla.com")
                .build());

        // when, then
        assertThatThrownBy(() -> memberStyleService.readMemberStyle(member.getId()))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("스타일이 등록되지 않은 회원입니다");
    }

    @Test
    void 존재하지_않는_회원_식별자로_스타일조회시_예외가_발생한다() {
        // given
        Member member = memberRepository.save(Member.builder()
                .memberType(MemberType.APPLE)
                .oauthEmail("bookbla@bookbla.com")
                .build());

        // when, then
        assertThatThrownBy(() -> memberStyleService.readMemberStyle(member.getId()))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("스타일이 등록되지 않은 회원입니다");
    }

    @Test
    void 회원_스타일을_업데이트_할_수_있다() {
        // given
        Member member = memberRepository.save(Member.builder()
                .memberType(MemberType.APPLE)
                .oauthEmail("bookbla@bookbla.com")
                .memberStatus(MemberStatus.COMPLETED)
                .memberStyle(
                        MemberStyle.builder()
                                .smokeType(SMOKE)
                                .mbti(INTP)
                                .build()
                ).build());
        MemberStyleUpdateRequest memberStyleUpdateRequest = new MemberStyleUpdateRequest(
                "infj", "가끔", 160, profileImageType.getId()
        );

        // when
        memberStyleService.updateMemberStyle(member.getId(), memberStyleUpdateRequest);

        // then
        MemberStyle memberStyle = memberRepository.getByIdOrThrow(member.getId()).getMemberStyle();
        assertAll(
                () -> assertThat(memberStyle.getMbti()).isEqualTo(INFJ),
                () -> assertThat(memberStyle.getSmokeType()).isEqualTo(SOMETIMES),
                () -> assertThat(memberStyle.getHeight()).isEqualTo(160),
                () -> assertThat(memberStyle.getProfileImageType().getId()).isEqualTo(profileImageType.getId())
        );
    }

    @Test
    void 존재하지_않는_회원_식별자로_회원_스타일_수정시_예외가_발생한다() {
        // given
        Long nonMemberId = -999999L;
        MemberStyleUpdateRequest memberStyleUpdateRequest = new MemberStyleUpdateRequest(
                "infj", "가끔", 160, 1L
        );

        // when, then
        assertThatThrownBy(
                () -> memberStyleService.updateMemberStyle(nonMemberId, memberStyleUpdateRequest))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("해당 식별자를 가진 회원이 존재하지 않습니다");
    }

    @Test
    void 회원_스타일이_저장되지_않은_회원의_스타일_업데이트시_예외가_발생한다() {
        // given
        Member member = memberRepository.save(Member.builder()
                .memberType(MemberType.APPLE)
                .oauthEmail("bookbla@bookbla.com")
                .build());
        MemberStyleUpdateRequest memberStyleUpdateRequest = new MemberStyleUpdateRequest(
                "infj", "가끔", 160, 1L
        );

        // when, then
        assertThatThrownBy(() -> memberStyleService.updateMemberStyle(member.getId(),
                memberStyleUpdateRequest))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("스타일이 등록되지 않은 회원입니다");
    }

    @AfterEach
    void tearDown() {
        memberRepository.deleteAll();
        profileImageTypeRepository.deleteAllInBatch();
    }
}
