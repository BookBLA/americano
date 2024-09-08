package com.bookbla.americano.domain.school.service;

import java.util.Optional;

import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.school.controller.dto.request.InvitationCodeEntryRequest;
import com.bookbla.americano.domain.school.repository.InvitationRepository;
import com.bookbla.americano.domain.school.repository.entity.Invitation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.bookbla.americano.domain.school.repository.entity.InvitationStatus.PENDING;
import static com.bookbla.americano.domain.school.repository.entity.InvitationType.FESTIVAL;
import static com.bookbla.americano.domain.school.repository.entity.InvitationType.MAN;
import static com.bookbla.americano.domain.school.repository.entity.InvitationType.WOMAN;
import static com.bookbla.americano.fixture.Fixture.스타일_등록_완료_남성_고도리;
import static com.bookbla.americano.fixture.Fixture.프로필_등록_완료_남성_리준희;
import static com.bookbla.americano.fixture.Fixture.프로필_등록_완료_여성_김밤비;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
class InvitationServiceTest {

    @Autowired
    private InvitationService sut;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private InvitationRepository invitationRepository;

    @Test
    void 축제_초대코드를_입력하면_축제_초대가_생성된다() {
        Member member = memberRepository.save(스타일_등록_완료_남성_고도리);
        var request = new InvitationCodeEntryRequest("JUST4YOU");

        // when
        sut.entryInvitationCode(member.getId(), request);

        // then
        Optional<Invitation> maybeInvitation = invitationRepository.findByInvitedMemberId(member.getId());
        assertAll(
                () -> assertThat(maybeInvitation).isNotNull(),
                () -> assertThat(maybeInvitation.get().getInvitedMemberId()).isEqualTo(member.getId()),
                () -> assertThat(maybeInvitation.get().getInvitingMemberId()).isEqualTo(0L),
                () -> assertThat(maybeInvitation.get().getInvitationStatus()).isEqualTo(PENDING),
                () -> assertThat(maybeInvitation.get().getInvitationType()).isEqualTo(FESTIVAL)
        );
    }


    @Test
    void 남성_회원이_초대코드를_입력하면_남성_초대가_생성된다() {
        // given
        Member man1 = memberRepository.save(스타일_등록_완료_남성_고도리);
        Member man2 = memberRepository.save(프로필_등록_완료_남성_리준희);

        var request = new InvitationCodeEntryRequest("고도리초대코드");

        // when
        sut.entryInvitationCode(man2.getId(), request);

        // then
        Optional<Invitation> maybeInvitation = invitationRepository.findByInvitedMemberId(man2.getId());
        assertAll(
                () -> assertThat(maybeInvitation).isNotNull(),
                () -> assertThat(maybeInvitation.get().getInvitedMemberId()).isEqualTo(man2.getId()),
                () -> assertThat(maybeInvitation.get().getInvitingMemberId()).isEqualTo(man1.getId()),
                () -> assertThat(maybeInvitation.get().getInvitationStatus()).isEqualTo(PENDING),
                () -> assertThat(maybeInvitation.get().getInvitationType()).isEqualTo(MAN)
        );
    }

    @Test
    void 여성_회원이_초대코드를_입력하면_여성_초대가_생성된다() {
        // given
        Member man = memberRepository.save(스타일_등록_완료_남성_고도리);
        Member woman = memberRepository.save(프로필_등록_완료_여성_김밤비);

        var request = new InvitationCodeEntryRequest("고도리초대코드");

        // when
        sut.entryInvitationCode(woman.getId(), request);

        // then
        Optional<Invitation> maybeInvitation = invitationRepository.findByInvitedMemberId(woman.getId());
        assertAll(
                () -> assertThat(maybeInvitation).isNotNull(),
                () -> assertThat(maybeInvitation.get().getInvitedMemberId()).isEqualTo(woman.getId()),
                () -> assertThat(maybeInvitation.get().getInvitingMemberId()).isEqualTo(man.getId()),
                () -> assertThat(maybeInvitation.get().getInvitationStatus()).isEqualTo(PENDING),
                () -> assertThat(maybeInvitation.get().getInvitationType()).isEqualTo(WOMAN)
        );
    }

    @AfterEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
        invitationRepository.deleteAllInBatch();
    }
}
