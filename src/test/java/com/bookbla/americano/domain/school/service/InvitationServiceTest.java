package com.bookbla.americano.domain.school.service;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.admin.event.AdminNotificationEventListener;
import com.bookbla.americano.domain.member.controller.dto.response.MemberInvitationRewardResponse;
import com.bookbla.americano.domain.member.repository.MemberBookmarkRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBookmark;
import com.bookbla.americano.domain.school.controller.dto.request.InvitationCodeEntryRequest;
import com.bookbla.americano.domain.school.exception.InvitationExceptionType;
import com.bookbla.americano.domain.school.repository.InvitationRepository;
import com.bookbla.americano.domain.school.repository.entity.Invitation;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import static com.bookbla.americano.domain.school.repository.entity.InvitationStatus.BOOKMARK;
import static com.bookbla.americano.domain.school.repository.entity.InvitationType.*;
import static com.bookbla.americano.fixture.Fixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
class InvitationServiceTest {

    @Autowired
    private InvitationService sut;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberBookmarkRepository memberBookmarkRepository;

    @Autowired
    private InvitationRepository invitationRepository;

    @MockBean
    private AdminNotificationEventListener adminNotificationEventListener;

    @Nested
    class 초대코드_입력_성공 {

        @Test
        void 축제_초대코드를_입력하면_축제_초대가_생성된다() {
            Long festivalInvitingMemberId = 0L;
            Member member = memberRepository.save(스타일_등록_완료_남성_고도리);
            memberBookmarkRepository.save(MemberBookmark.builder().member(member).build());
            var request = new InvitationCodeEntryRequest("JUST4YOU");

            // when
            sut.entryInvitationCode(member.getId(), request);

            // then
            Invitation invitation = invitationRepository.findByInvitedMemberId(member.getId()).orElseThrow();
            assertAll(
                    () -> assertThat(invitation).isNotNull(),
                    () -> assertThat(invitation.getInvitedMemberId()).isEqualTo(member.getId()),
                    () -> assertThat(invitation.getInvitingMemberId()).isEqualTo(festivalInvitingMemberId),
                    () -> assertThat(invitation.getInvitationStatus()).isEqualTo(BOOKMARK),
                    () -> assertThat(invitation.getInvitationType()).isEqualTo(FESTIVAL)
            );
        }

        @Test
        void 축제_초대코드를_입력하면_책갈피를_지급한다() {
            Member member = memberRepository.save(스타일_등록_완료_남성_고도리);
            MemberBookmark memberBookmark = memberBookmarkRepository.save(MemberBookmark.builder().member(member).build());
            var request = new InvitationCodeEntryRequest("JUST4YOU");

            // when
            sut.entryInvitationCode(member.getId(), request);

            // then
            assertThat(memberBookmark.getBookmarkCount()).isEqualTo(105);
        }

        @Test
        void 남성_회원이_초대코드를_입력하면_남성_초대가_생성된다() {
            // given
            Member man1 = memberRepository.save(스타일_등록_완료_남성_고도리);
            Member man2 = memberRepository.save(프로필_등록_완료_남성_리준희);
            memberBookmarkRepository.save(MemberBookmark.builder().member(man1).build());
            memberBookmarkRepository.save(MemberBookmark.builder().member(man2).build());

            var request = new InvitationCodeEntryRequest("고도리초대코드");

            // when
            sut.entryInvitationCode(man2.getId(), request);

            // then
            Invitation invitation = invitationRepository.findByInvitedMemberId(man2.getId()).orElseThrow();
            assertAll(
                    () -> assertThat(invitation).isNotNull(),
                    () -> assertThat(invitation.getInvitedMemberId()).isEqualTo(man2.getId()),
                    () -> assertThat(invitation.getInvitingMemberId()).isEqualTo(man1.getId()),
                    () -> assertThat(invitation.getInvitationStatus()).isEqualTo(BOOKMARK),
                    () -> assertThat(invitation.getInvitationType()).isEqualTo(MAN)
            );
        }

        @Test
        void 남성_회원_초대코드_지급시_책갈피를_지급한다() {
            // given
            Member man1 = memberRepository.save(스타일_등록_완료_남성_고도리);
            Member man2 = memberRepository.save(프로필_등록_완료_남성_리준희);
            MemberBookmark man1MemberBookmark = memberBookmarkRepository.save(MemberBookmark.builder().member(man1).build());
            MemberBookmark man2MemberBookmark = memberBookmarkRepository.save(MemberBookmark.builder().member(man2).build());

            var request = new InvitationCodeEntryRequest("고도리초대코드");

            // when
            sut.entryInvitationCode(man2.getId(), request);

            // then
            assertAll(
                    () -> assertThat(man1MemberBookmark.getBookmarkCount()).isEqualTo(35),
                    () -> assertThat(man2MemberBookmark.getBookmarkCount()).isEqualTo(35)
            );
        }

        @Test
        void 여성_회원이_초대코드를_입력하면_여성_초대가_생성된다() {
            // given
            Member man = memberRepository.save(스타일_등록_완료_남성_고도리);
            Member woman = memberRepository.save(프로필_등록_완료_여성_김밤비);
            memberBookmarkRepository.save(MemberBookmark.builder().member(man).build());
            memberBookmarkRepository.save(MemberBookmark.builder().member(woman).build());

            var request = new InvitationCodeEntryRequest("고도리초대코드");

            // when
            sut.entryInvitationCode(woman.getId(), request);

            // then
            Invitation invitation = invitationRepository.findByInvitedMemberId(woman.getId()).orElseThrow();
            assertAll(
                    () -> assertThat(invitation.getInvitedMemberId()).isEqualTo(woman.getId()),
                    () -> assertThat(invitation.getInvitingMemberId()).isEqualTo(man.getId()),
                    () -> assertThat(invitation.getInvitationStatus()).isEqualTo(BOOKMARK),
                    () -> assertThat(invitation.getInvitationType()).isEqualTo(WOMAN)
            );
        }

        @Test
        void 여성_회원이_초대코드를_입력하면_책갈피_지급한다() {
            // given
            Member man = memberRepository.save(스타일_등록_완료_남성_고도리);
            Member woman = memberRepository.save(프로필_등록_완료_여성_김밤비);
            MemberBookmark manMemberBookmark = memberBookmarkRepository.save(MemberBookmark.builder().member(man).build());
            MemberBookmark womanMemberBookmark = memberBookmarkRepository.save(MemberBookmark.builder().member(woman).build());

            var request = new InvitationCodeEntryRequest("고도리초대코드");

            // when
            sut.entryInvitationCode(woman.getId(), request);

            // then
            assertAll(
                    () -> assertThat(manMemberBookmark.getBookmarkCount()).isEqualTo(70),
                    () -> assertThat(womanMemberBookmark.getBookmarkCount()).isEqualTo(70)
            );
        }
    }

    @Nested
    class 초대코드_입력_실패 {

        @Test
        void 본인의_초대코드를_입력하는_경우_예외가_발생한다() {
            // given
            Member 고도리 = memberRepository.save(스타일_등록_완료_남성_고도리);
            memberBookmarkRepository.save(MemberBookmark.builder().member(고도리).build());
            var request = new InvitationCodeEntryRequest("고도리초대코드");

            // when, then
            assertThatThrownBy(() -> sut.entryInvitationCode(고도리.getId(), request))
                    .isInstanceOf(BaseException.class)
                    .hasMessageContaining(InvitationExceptionType.INVALID_INVITATION_CODE_MYSELF.getMessage());
        }

        @Test
        void 초대코드_입력_성공_후_다시_초대코드를_입력하는_경우_예외가_발생한다() {
            // given
            Member 고도리 = memberRepository.save(스타일_등록_완료_남성_고도리);
            Member 김밤비 = memberRepository.save(프로필_등록_완료_여성_김밤비);
            memberBookmarkRepository.save(MemberBookmark.builder().member(고도리).build());
            memberBookmarkRepository.save(MemberBookmark.builder().member(김밤비).build());

            var request = new InvitationCodeEntryRequest("고도리초대코드");
            sut.entryInvitationCode(김밤비.getId(), request);

            // when, then
            assertThatThrownBy(() -> sut.entryInvitationCode(김밤비.getId(), request))
                    .isInstanceOf(BaseException.class)
                    .hasMessageContaining(InvitationExceptionType.INVITATION_EXISTS.getMessage());
        }
    }

    @Nested
    class 초대보상_모달 {

        @Test
        void 초대하지_않았다면_초대보상을_받을_수_없다() {
            // given
            Member member = memberRepository.save(Member.builder().build());

            // when
            MemberInvitationRewardResponse response = sut.getInvitationRewardStatus(member.getId());

            // then
            assertThat(response.getInvitingRewardStatus()).isFalse();
            assertThat(response.getInvitedRewardStatus()).isFalse();
        }

        @Test
        void 초대되었다면_초대보상을_받을_수_있다() {
            // given
            Member man1 = memberRepository.save(스타일_등록_완료_남성_고도리);
            Member man2 = memberRepository.save(프로필_등록_완료_남성_리준희);
            MemberBookmark man1MemberBookmark = memberBookmarkRepository.save(MemberBookmark.builder().member(man1).build());
            MemberBookmark man2MemberBookmark = memberBookmarkRepository.save(MemberBookmark.builder().member(man2).build());

            // when
            sut.entryInvitationCode(man2.getId(), new InvitationCodeEntryRequest("고도리초대코드"));
            MemberInvitationRewardResponse response = sut.getInvitationRewardStatus(man2.getId());

            // then
            assertThat(response.getInvitingRewardStatus()).isFalse();
            assertThat(response.getInvitedRewardStatus()).isTrue();
            assertThat(response.getInvitedMembersGender()).isNull();
        }

        @Test
        void 초대했다면_초대보상을_받을_수_있다() {
            // given
            Member man1 = memberRepository.save(프로필_등록_완료_여성_김밤비);
            Member man2 = memberRepository.save(프로필_등록_완료_남성_리준희);
            MemberBookmark man1MemberBookmark = memberBookmarkRepository.save(MemberBookmark.builder().member(man1).build());
            MemberBookmark man2MemberBookmark = memberBookmarkRepository.save(MemberBookmark.builder().member(man2).build());

            // when
            sut.entryInvitationCode(man2.getId(), new InvitationCodeEntryRequest("김밤비초대코드"));
            MemberInvitationRewardResponse response = sut.getInvitationRewardStatus(man1.getId());

            // then
            assertThat(response.getInvitingRewardStatus()).isTrue();
            assertThat(response.getInvitedRewardStatus()).isFalse();
            assertThat(response.getInvitedMembersGender()).isEqualTo(man2.getMemberProfile().getGender().name());
        }
    }
}

