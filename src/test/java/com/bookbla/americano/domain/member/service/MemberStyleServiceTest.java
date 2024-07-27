package com.bookbla.americano.domain.member.service;

import static com.bookbla.americano.domain.member.enums.ContactType.FAST;
import static com.bookbla.americano.domain.member.enums.ContactType.SLOW;
import static com.bookbla.americano.domain.member.enums.DateCostType.DATE_ACCOUNT;
import static com.bookbla.americano.domain.member.enums.DateCostType.DUTCH_PAY;
import static com.bookbla.americano.domain.member.enums.DateStyleType.HOME;
import static com.bookbla.americano.domain.member.enums.DrinkType.EVERYDAY;
import static com.bookbla.americano.domain.member.enums.DrinkType.NONE;
import static com.bookbla.americano.domain.member.enums.JustFriendType.ALCOHOL;
import static com.bookbla.americano.domain.member.enums.JustFriendType.NEVER;
import static com.bookbla.americano.domain.member.enums.Mbti.INFJ;
import static com.bookbla.americano.domain.member.enums.Mbti.INTP;
import static com.bookbla.americano.domain.member.enums.SmokeType.NON_SMOKE;
import static com.bookbla.americano.domain.member.enums.SmokeType.SMOKE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.controller.dto.request.MemberStyleCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberStyleUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberStyleResponse;
import com.bookbla.americano.domain.member.enums.HeightType;
import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.bookbla.americano.domain.member.enums.MemberType;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberStyle;
import com.bookbla.americano.domain.memberask.repository.MemberAskRepository;
import com.bookbla.americano.domain.memberask.repository.entity.MemberAsk;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberStyleServiceTest {

    @Autowired
    private MemberStyleService memberStyleService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberAskRepository memberAskRepository;

    @Test
    void 회원의_스타일을_생성할_수_있다() {
        // given
        Member member = memberRepository.save(Member.builder()
                .memberType(MemberType.APPLE)
                .oauthEmail("bookbla@bookbla.com")
                .build());
        MemberStyleCreateRequest memberStyleCreateRequest = new MemberStyleCreateRequest(
                "infj", "매일", "더치페이", "비흡연", "느긋이", "허용 X", "집 데이트", "160cm 이상 ~ 165cm 미만",
                "주로 어디서 책을 읽는 편이세요?"
        );

        // when
        MemberStyleResponse memberStyleResponse = memberStyleService.createMemberStyle(
                member.getId(),
                memberStyleCreateRequest);

        // then
        assertAll(
                () -> assertThat(memberStyleResponse.getMemberId()).isNotNull(),
                () -> assertThat(memberStyleResponse.getMbti()).isEqualToIgnoringCase(INFJ.name()),
                () -> assertThat(memberStyleResponse.getDrinkType()).isEqualTo(
                        EVERYDAY.getDetailValue()),
                () -> assertThat(memberStyleResponse.getDateCostType()).isEqualTo(
                        DUTCH_PAY.getDetailValue()),
                () -> assertThat(memberStyleResponse.getSmokeType()).isEqualTo(
                        NON_SMOKE.getDetailValue()),
                () -> assertThat(memberStyleResponse.getContactType()).isEqualTo(
                        SLOW.getDetailValue()),
                () -> assertThat(memberStyleResponse.getJustFriendType()).isEqualTo(
                        NEVER.getDetailValue()),
                () -> assertThat(memberStyleResponse.getHeightType()).isEqualTo(
                        HeightType.OVER_160_AND_LESS_THAN_165.getDetailValue()),
                () -> assertThat(memberStyleResponse.getDateStyleType()).isEqualTo(
                        HOME.getDetailValue()),
                () -> assertThat(memberStyleResponse.getMemberAsk()).isEqualTo("주로 어디서 책을 읽는 편이세요?")
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
                        .contactType(FAST)
                        .drinkType(NONE)
                        .smokeType(SMOKE)
                        .justFriendType(ALCOHOL)
                        .dateStyleType(HOME)
                        .heightType(HeightType.LESS_THAN_150)
                        .mbti(INTP)
                        .dateCostType(DATE_ACCOUNT)
                        .build())
                .build());
        memberAskRepository.save(MemberAsk
                .builder()
                .member(member)
                .contents("주로 어디서 책을 읽으세요?")
                .build());

        // when
        MemberStyleResponse memberStyleResponse = memberStyleService.readMemberStyle(
                member.getId());

        // then
        assertAll(
                () -> assertThat(memberStyleResponse.getMemberId()).isNotNull(),
                () -> assertThat(memberStyleResponse.getSmokeType()).isEqualTo(
                        SMOKE.getDetailValue()),
                () -> assertThat(memberStyleResponse.getContactType()).isEqualTo(
                        FAST.getDetailValue()),
                () -> assertThat(memberStyleResponse.getDateCostType()).isEqualTo(
                        DATE_ACCOUNT.getDetailValue()),
                () -> assertThat(memberStyleResponse.getDateStyleType()).isEqualTo(
                        HOME.getDetailValue()),
                () -> assertThat(memberStyleResponse.getJustFriendType()).isEqualTo(
                        ALCOHOL.getDetailValue()),
                () -> assertThat(memberStyleResponse.getHeightType()).isEqualTo(
                        HeightType.LESS_THAN_150.getDetailValue()),
                () -> assertThat(memberStyleResponse.getDrinkType()).isEqualTo(
                        NONE.getDetailValue()),
                () -> assertThat(memberStyleResponse.getMbti()).isEqualToIgnoringCase(INTP.name()),
                () -> assertThat(memberStyleResponse.getMemberAsk()).isEqualTo("주로 어디서 책을 읽으세요?")
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
                                .contactType(FAST)
                                .drinkType(NONE)
                                .smokeType(SMOKE)
                                .justFriendType(ALCOHOL)
                                .dateStyleType(HOME)
                                .mbti(INTP)
                                .dateCostType(DATE_ACCOUNT)
                                .build()
                ).build());
        memberAskRepository.save(MemberAsk.builder()
                .member(member)
                .contents("어느 시간대에 책을 읽으시나요?")
                .build());
        MemberStyleUpdateRequest memberStyleUpdateRequest = new MemberStyleUpdateRequest(
                "infj", "매일", "더치페이", "비흡연", "느긋이", "허용 X", "집 데이트", "160cm 이상 ~ 165cm 미만",
                "주로 어디서 책을 읽는 편이세요?"
        );

        // when
        memberStyleService.updateMemberStyle(member.getId(), memberStyleUpdateRequest);

        // then
        MemberStyle memberStyle = memberRepository.getByIdOrThrow(member.getId()).getMemberStyle();
        assertAll(
                () -> assertThat(memberStyle.getDateStyleType()).isEqualTo(HOME),
                () -> assertThat(memberStyle.getMbti()).isEqualTo(INFJ),
                () -> assertThat(memberStyle.getDrinkType()).isEqualTo(EVERYDAY),
                () -> assertThat(memberStyle.getDateCostType()).isEqualTo(DUTCH_PAY),
                () -> assertThat(memberStyle.getSmokeType()).isEqualTo(NON_SMOKE),
                () -> assertThat(memberStyle.getContactType()).isEqualTo(SLOW),
                () -> assertThat(memberStyle.getJustFriendType()).isEqualTo(NEVER)
        );
    }

    @Test
    void 존재하지_않는_회원_식별자로_회원_스타일_수정시_예외가_발생한다() {
        // given
        Long nonMemberId = -999999L;
        MemberStyleUpdateRequest memberStyleUpdateRequest = new MemberStyleUpdateRequest(
                "infj", "매일", "더치페이", "비흡연", "느긋이", "허용 X", "집 데이트", "160cm 이상~165cm 미만",
                "주로 어디서 책을 읽으세요?"
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
                "infj", "매일", "더치페이", "비흡연", "느긋이", "허용 X", "집 데이트", "160cm 이상~165cm 미만",
                "주로 어디서 책을 읽으세요?"
        );

        // when, then
        assertThatThrownBy(() -> memberStyleService.updateMemberStyle(member.getId(),
                memberStyleUpdateRequest))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("스타일이 등록되지 않은 회원입니다");
    }

    @AfterEach
    void tearDown() {
        memberAskRepository.deleteAll();
        memberRepository.deleteAll();
    }
}
