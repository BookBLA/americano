package com.bookbla.americano.domain.admin.service;

import com.bookbla.americano.domain.admin.service.dto.StatusUpdateDto;
import com.bookbla.americano.domain.member.enums.MemberVerifyStatus;
import com.bookbla.americano.domain.member.enums.OpenKakaoRoomStatus;
import com.bookbla.americano.domain.member.enums.ProfileImageStatus;
import com.bookbla.americano.domain.member.enums.StudentIdImageStatus;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.MemberVerifyRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import com.bookbla.americano.domain.member.repository.entity.MemberVerify;
import com.bookbla.americano.domain.notification.service.AlarmService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.bookbla.americano.domain.member.enums.Gender.MALE;
import static com.bookbla.americano.domain.member.enums.MemberStatus.APPROVAL;
import static com.bookbla.americano.domain.member.enums.MemberType.ADMIN;
import static com.bookbla.americano.domain.member.enums.MemberVerifyStatus.PENDING;
import static com.bookbla.americano.domain.member.enums.MemberVerifyStatus.SUCCESS;
import static com.bookbla.americano.domain.member.enums.MemberVerifyType.OPEN_KAKAO_ROOM_URL;
import static com.bookbla.americano.domain.member.enums.MemberVerifyType.PROFILE_IMAGE;
import static com.bookbla.americano.domain.member.enums.MemberVerifyType.STUDENT_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
class AdminVerificationServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberVerifyRepository memberVerifyRepository;

    @MockBean
    private AlarmService alarmService;

    @Autowired
    private AdminVerificationService adminVerificationService;

    @Test
    void 회원의_카카오톡_오픈채팅방_인증_상태를_변경할_수_있다() {
        // given
        Member member = memberRepository.save(Member.builder()
                .memberType(ADMIN)
                .memberStatus(APPROVAL)
                .oauthEmail("bookbla@bookbla.com")
                .memberProfile(MemberProfile.builder().name("문성진").profileImageUrl("프사3").phoneNumber("01012345678").openKakaoRoomUrl("비밀링크").gender(MALE).name("이준희").build())
                .build());
        MemberVerify memberVerify = memberVerifyRepository.save(MemberVerify.builder()
                .memberId(member.getId())
                .verifyType(OPEN_KAKAO_ROOM_URL)
                .verifyStatus(MemberVerifyStatus.PENDING)
                .build());
        StatusUpdateDto statusUpdateDto = new StatusUpdateDto(memberVerify.getId(), "done", "성공");

        // when
        adminVerificationService.updateMemberKakaoRoomStatus(statusUpdateDto);

        // then
        MemberProfile memberProfile = memberRepository.getByIdOrThrow(member.getId()).getMemberProfile();
        MemberVerify foundMemberVerify = memberVerifyRepository.getByIdOrThrow(memberVerify.getId());
        assertAll(
                () -> assertThat(memberProfile.getOpenKakaoRoomStatus()).isEqualTo(OpenKakaoRoomStatus.DONE),
                () -> assertThat(foundMemberVerify.getVerifyStatus()).isEqualTo(SUCCESS)
        );
    }

    @Test
    void 회원의_프로필_사진_인증_상태를_변경할_수_있다() {
        // given
        Member member = memberRepository.save(Member.builder()
                .memberType(ADMIN)
                .oauthEmail("bookbla@bookbla.com")
                .memberStatus(APPROVAL)
                .memberProfile(MemberProfile.builder().name("문성진").profileImageStatus(ProfileImageStatus.PENDING).profileImageUrl("프사3").phoneNumber("01012345678").openKakaoRoomUrl("비밀링크").gender(MALE).build())
                .build());
        MemberVerify memberVerify = MemberVerify.builder()
                .memberId(member.getId())
                .contents("프로필 사진 링크입니다.")
                .verifyType(PROFILE_IMAGE)
                .verifyStatus(PENDING)
                .build();

        memberVerifyRepository.save(memberVerify);

        StatusUpdateDto statusUpdateDto = new StatusUpdateDto(memberVerify.getId(), "done", "성공");

        // when
        adminVerificationService.updateMemberImageStatus(statusUpdateDto);

        // then
        MemberProfile memberProfile = memberRepository.getByIdOrThrow(member.getId()).getMemberProfile();
        MemberVerify findMemberVerify = memberVerifyRepository.getByIdOrThrow(memberVerify.getId());
        assertAll(
                () -> assertThat(memberProfile.getProfileImageUrl()).isEqualTo("프로필 사진 링크입니다."),
                () -> assertThat(memberProfile.getProfileImageStatus()).isEqualTo(ProfileImageStatus.DONE),
                () -> assertThat(findMemberVerify.getVerifyStatus()).isEqualTo(SUCCESS)
        );
    }

    @Test
    void 회원의_학생증_인증_상태를_변경할_수_있다() {
        // given
        Member member = memberRepository.save(Member.builder()
                .memberType(ADMIN)
                .oauthEmail("bookbla@bookbla.com")
                .memberProfile(MemberProfile.builder().studentIdImageStatus(StudentIdImageStatus.PENDING).build())
                .memberStatus(APPROVAL)
                .pushAlarmEnabled(Boolean.TRUE)
                .build());
        MemberVerify memberVerify = memberVerifyRepository.save(MemberVerify.builder()
                .memberId(member.getId())
                .verifyStatus(PENDING)
                .verifyType(STUDENT_ID)
                .contents("학생증 링크~")
                .description("이름: 고도현, 학과: 관광경영학과, 학번: 201900001")
                .build());
        StatusUpdateDto statusUpdateDto = new StatusUpdateDto(memberVerify.getId(), "denial", "흐릿해요");

        // when
        adminVerificationService.updateMemberStudentIdStatus(statusUpdateDto);

        // then
        MemberProfile memberProfile = memberRepository.getByIdOrThrow(member.getId()).getMemberProfile();
        MemberVerify findMemberVerify = memberVerifyRepository.getByIdOrThrow(memberVerify.getId());
        assertAll(
                () -> assertThat(memberProfile.getStudentIdImageStatus()).isEqualTo(StudentIdImageStatus.DENIAL),
                () -> assertThat(findMemberVerify.getVerifyStatus()).isEqualTo(MemberVerifyStatus.FAIL),
                () -> assertThat(findMemberVerify.getDescription()).isEqualTo("흐릿해요")
        );
    }

    @AfterEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
        memberVerifyRepository.deleteAllInBatch();
    }
}
