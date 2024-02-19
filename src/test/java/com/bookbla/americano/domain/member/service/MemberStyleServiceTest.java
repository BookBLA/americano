package com.bookbla.americano.domain.member.service;

import static com.bookbla.americano.domain.member.enums.ContactType.FAST;
import static com.bookbla.americano.domain.member.enums.ContactType.SLOW;
import static com.bookbla.americano.domain.member.enums.DateCostType.DATE_ACCOUNT;
import static com.bookbla.americano.domain.member.enums.DateCostType.DUTCH_PAY;
import static com.bookbla.americano.domain.member.enums.DateStyle.HOME;
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
import com.bookbla.americano.domain.member.Member;
import com.bookbla.americano.domain.member.MemberStyle;
import com.bookbla.americano.domain.member.controller.dto.request.MemberStyleCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.StylesResponse;
import com.bookbla.americano.domain.member.enums.MemberType;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.MemberStyleRepository;
import com.bookbla.americano.domain.member.controller.dto.response.MemberStyleResponse;
import com.bookbla.americano.domain.member.controller.dto.request.MemberStyleUpdateRequest;
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
    private MemberStyleRepository memberStyleRepository;

    @Test
    void 회원_스타일_목록을_확인할_수_있다() {
        // when
        StylesResponse stylesResponse = memberStyleService.readStyles();

        // then
        assertAll(
                () -> assertThat(stylesResponse.getSmokeTypes()).containsExactly("흡연", "비흡연", "가끔"),
                () -> assertThat(stylesResponse.getDrinkTypes()).containsExactly("안마심", "월 1~2회",
                        "주 1회", "주 1회 이상", "매일"),
                () -> assertThat(stylesResponse.getContactTypes()).containsExactly("느긋이", "칼답"),
                () -> assertThat(stylesResponse.getDateStyleTypes()).containsExactly("집 데이트",
                        "야외 데이트"),
                () -> assertThat(stylesResponse.getDateCostTypes()).containsExactly("더치페이",
                        "번갈아가면서 사기", "여유있는 사람이 좀 더", "데이트 통장"),
                () -> assertThat(stylesResponse.getJustFriendTypes()).containsExactly("허용 X",
                        "단 둘이 밥 먹기", "단 둘이 술 먹기", "단 둘이 여행 가기", "상관 없음")
        );
    }

    @Test
    void 회원의_스타일을_생성할_수_있다() {
        // given
        Member member = memberRepository.save(Member.builder()
                .name("이준희")
                .memberType(MemberType.APPLE)
                .oauthEmail("bookbla@bookbla.com")
                .build());
        MemberStyleCreateRequest memberStyleCreateRequest = new MemberStyleCreateRequest(
                "infj", "매일", "더치페이", "비흡연", "느긋이", "허용 X", "집 데이트"
        );

        // when
        MemberStyleResponse memberStyleResponse = memberStyleService.createMemberStyle(member.getId(),
                memberStyleCreateRequest);

        // then
        assertAll(
                () -> assertThat(memberStyleResponse.getMemberId()).isNotNull(),
                () -> assertThat(memberStyleResponse.getName()).isEqualTo("이준희"),
                () -> assertThat(memberStyleResponse.getMemberStyleId()).isNotNull(),
                () -> assertThat(memberStyleResponse.getMbti()).isEqualToIgnoringCase("infj"),
                () -> assertThat(memberStyleResponse.getDrinkType()).isEqualTo("매일"),
                () -> assertThat(memberStyleResponse.getDateCostType()).isEqualTo("더치페이"),
                () -> assertThat(memberStyleResponse.getSmokeType()).isEqualTo("비흡연"),
                () -> assertThat(memberStyleResponse.getContactType()).isEqualTo("느긋이"),
                () -> assertThat(memberStyleResponse.getJustFriendType()).isEqualTo("허용 X"),
                () -> assertThat(memberStyleResponse.getDateStyleType()).isEqualTo("집 데이트")
        );
    }


    @Test
    void 회원의_스타일을_조회할_수_있다() {
        // given
        Member member = memberRepository.save(Member.builder()
                .name("이준희")
                .memberType(MemberType.APPLE)
                .oauthEmail("bookbla@bookbla.com")
                .build());
        memberStyleRepository.save(MemberStyle.builder()
                .member(member)
                .contactType(FAST)
                .drinkType(NONE)
                .smokeType(SMOKE)
                .justFriendType(ALCOHOL)
                .dateStyle(HOME)
                .mbti(INTP)
                .dateCostType(DATE_ACCOUNT)
                .build());

        // when
        MemberStyleResponse memberStyleResponse = memberStyleService.readMemberStyle(
                member.getId());

        // then
        assertAll(
                () -> assertThat(memberStyleResponse.getMemberId()).isNotNull(),
                () -> assertThat(memberStyleResponse.getName()).isEqualTo("이준희"),
                () -> assertThat(memberStyleResponse.getSmokeType()).isEqualTo("흡연"),
                () -> assertThat(memberStyleResponse.getContactType()).isEqualTo("칼답"),
                () -> assertThat(memberStyleResponse.getDateCostType()).isEqualTo("데이트 통장"),
                () -> assertThat(memberStyleResponse.getDateStyleType()).isEqualTo("집 데이트"),
                () -> assertThat(memberStyleResponse.getJustFriendType()).isEqualTo("단 둘이 술 먹기"),
                () -> assertThat(memberStyleResponse.getDrinkType()).isEqualTo("안마심"),
                () -> assertThat(memberStyleResponse.getMbti()).isEqualToIgnoringCase("intp")
        );
    }

    @Test
    void 스타일이_등록되지_않은_회원은_스타일_조회시_예외가_발생한다() {
        // given
        Member member = memberRepository.save(Member.builder()
                .name("이준희")
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
                .name("이준희")
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
                .name("이준희")
                .memberType(MemberType.APPLE)
                .oauthEmail("bookbla@bookbla.com")
                .build());
        memberStyleRepository.save(MemberStyle.builder()
                .member(member)
                .contactType(FAST)
                .drinkType(NONE)
                .smokeType(SMOKE)
                .justFriendType(ALCOHOL)
                .dateStyle(HOME)
                .mbti(INTP)
                .dateCostType(DATE_ACCOUNT)
                .build());
        MemberStyleUpdateRequest memberStyleUpdateRequest = new MemberStyleUpdateRequest(
                "infj", "매일", "더치페이", "비흡연", "느긋이", "허용 X", "집 데이트"
        );

        // when
        memberStyleService.updateMemberStyle(member.getId(), memberStyleUpdateRequest);

        // then
        MemberStyle memberStyle = memberStyleRepository.getByMemberOrThrow(member);
        assertAll(
                () -> assertThat(memberStyle.getDateStyle()).isEqualTo(HOME),
                () -> assertThat(memberStyle.getMbti()).isEqualTo(INFJ),
                () -> assertThat(memberStyle.getDrinkType()).isEqualTo(EVERYDAY),
                () -> assertThat(memberStyle.getDateCostType()).isEqualTo(DUTCH_PAY),
                () -> assertThat(memberStyle.getSmokeType()).isEqualTo(NON_SMOKE),
                () -> assertThat(memberStyle.getContactType()).isEqualTo(SLOW),
                () -> assertThat(memberStyle.getJustFriendType()).isEqualTo(NEVER)
        );
    }

    @Test
    void 회원_스타일이_저장되지_않은_회원의_스타일_업데이트시_예외가_발생한다() {
        // given
        Long nonExistMemberId = -999999L;
        MemberStyleUpdateRequest memberStyleUpdateRequest = new MemberStyleUpdateRequest(
                "infj", "매일", "더치페이", "비흡연", "느긋이", "허용 X", "집 데이트"
        );

        // when, then
        assertThatThrownBy(() -> memberStyleService.updateMemberStyle(nonExistMemberId, memberStyleUpdateRequest))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("해당 식별자를 가진 회원이 존재하지 않습니다");
    }

    @Test
    void 존재하지_않는_회원_식별자로_회원_스타일_수정시_예외가_발생한다() {
        // given
        Member member = memberRepository.save(Member.builder()
                .name("이준희")
                .memberType(MemberType.APPLE)
                .oauthEmail("bookbla@bookbla.com")
                .build());
        MemberStyleUpdateRequest memberStyleUpdateRequest = new MemberStyleUpdateRequest(
                "infj", "매일", "더치페이", "비흡연", "느긋이", "허용 X", "집 데이트"
        );

        // when, then
        assertThatThrownBy(() -> memberStyleService.updateMemberStyle(member.getId(), memberStyleUpdateRequest))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("스타일이 등록되지 않은 회원입니다");
    }

    @AfterEach
    void cleanUp() {
        memberStyleRepository.deleteAll();
        memberRepository.deleteAll();
    }
}
