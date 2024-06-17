package com.bookbla.americano.domain.admin.service;

import java.time.LocalDate;

import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberKakaoRoomResponses;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberProfileImageResponses;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberReadResponses;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberStudentIdResponses;
import com.bookbla.americano.domain.member.enums.MemberVerifyStatus;
import com.bookbla.americano.domain.member.enums.OpenKakaoRoomStatus;
import com.bookbla.americano.domain.member.enums.ProfileImageStatus;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.MemberVerifyRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import com.bookbla.americano.domain.member.repository.entity.MemberVerify;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import static com.bookbla.americano.domain.member.enums.Gender.MALE;
import static com.bookbla.americano.domain.member.enums.MemberStatus.APPROVAL;
import static com.bookbla.americano.domain.member.enums.MemberStatus.COMPLETED;
import static com.bookbla.americano.domain.member.enums.MemberStatus.STYLE;
import static com.bookbla.americano.domain.member.enums.MemberType.ADMIN;
import static com.bookbla.americano.domain.member.enums.MemberType.KAKAO;
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
class AdminMemberServiceTest {

    @Autowired
    private AdminMemberService adminMemberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberVerifyRepository memberVerifyRepository;

    @Test
    void 회원들의_정보를_확인할_수_있다() {
        // given
        Member member1 = Member.builder()
                .memberType(KAKAO)
                .memberStatus(COMPLETED)
                .oauthEmail("bookbla@bookbla.com")
                .memberProfile(MemberProfile.builder()
                        .birthDate(LocalDate.of(1999, 3, 3))
                        .phoneNumber("01012345678")
                        .gender(MALE)
                        .schoolName("가천대")
                        .name("이준희")
                        .build()
                ).build();
        memberRepository.save(member1);

        Member member2 = Member.builder()
                .memberType(KAKAO)
                .memberStatus(STYLE)
                .oauthEmail("bookbla@bookbla.com")
                .memberProfile(MemberProfile.builder().birthDate(LocalDate.of(1999, 3, 3)).phoneNumber("01012345678").gender(MALE).schoolName("가천대").name("이준희").build())
                .build();
        memberRepository.save(member2);

        Member member3 = Member.builder()
                .memberType(KAKAO)
                .memberStatus(APPROVAL)
                .oauthEmail("bookbla@bookbla.com")
                .memberProfile(MemberProfile.builder().phoneNumber("01012345678").gender(MALE).schoolName("가천대").name("이준희").build())
                .build();
        memberRepository.save(member3);

        // when
        AdminMemberReadResponses adminMemberReadResponses = adminMemberService.readMembers(PageRequest.of(0, 10));
        AdminMemberReadResponses.AdminMemberReadResponse adminMemberReadResponse = adminMemberReadResponses.getData().get(0);
        // then
        assertAll(
                () -> assertThat(adminMemberReadResponses.getTotalCount()).isEqualTo(3),
                () -> assertThat(adminMemberReadResponses.getData()).hasSize(3),
                () -> assertThat(adminMemberReadResponse.getMemberType()).isEqualToIgnoringCase("kakao"),
                () -> assertThat(adminMemberReadResponse.getAuthEmail()).isEqualTo("bookbla@bookbla.com"),
                () -> assertThat(adminMemberReadResponse.getName()).isEqualTo("이준희"),
                () -> assertThat(adminMemberReadResponse.getGender()).isEqualToIgnoringCase("male"),
                () -> assertThat(adminMemberReadResponse.getSchool()).isEqualTo("가천대"),
                () -> assertThat(adminMemberReadResponse.getBirthDate()).isEqualTo(LocalDate.of(1999, 3, 3)),
                () -> assertThat(adminMemberReadResponse.getPhone()).isEqualTo("01012345678")
        );
    }

    @Test
    void 오픈카톡방_승인_대기중인_회원들을_조회할_수_있다() {
        // given
        Member pendingMember = Member.builder()
                .memberType(ADMIN)
                .memberStatus(APPROVAL)
                .oauthEmail("bookbla@bookbla.com")
                .memberProfile(MemberProfile.builder().openKakaoRoomStatus(OpenKakaoRoomStatus.PENDING).name("이준희").profileImageUrl("프사1").phoneNumber("01012345678").openKakaoRoomUrl("비밀링크").gender(MALE).schoolName("가천대").name("이준희").build())
                .build();
        memberRepository.save(pendingMember);
        memberVerifyRepository.save(MemberVerify.builder()
                .memberId(pendingMember.getId())
                .contents("링크링크")
                .verifyType(OPEN_KAKAO_ROOM_URL)
                .build());

        Member pendingMember2 = Member.builder()
                .memberType(ADMIN)
                .memberStatus(APPROVAL)
                .oauthEmail("bookbla@bookbla.com")
                .memberProfile(MemberProfile.builder().name("김진호").openKakaoRoomStatus(OpenKakaoRoomStatus.NOT_DEFAULT).profileImageUrl("프사2").phoneNumber("01012345678").openKakaoRoomUrl("비밀링크").gender(MALE).schoolName("가천대").name("이준희").build())
                .build();
        memberRepository.save(pendingMember2);
        memberVerifyRepository.save(MemberVerify.builder()
                .memberId(pendingMember2.getId())
                .contents("링크링크")
                .verifyType(OPEN_KAKAO_ROOM_URL)
                .build());

        Member completedMember = Member.builder()
                .memberType(ADMIN)
                .memberStatus(COMPLETED)
                .oauthEmail("bookbla@bookbla.com")
                .memberProfile(MemberProfile.builder().name("문성진").openKakaoRoomStatus(OpenKakaoRoomStatus.DONE).profileImageUrl("프사3").phoneNumber("01012345678").openKakaoRoomUrl("비밀링크").gender(MALE).schoolName("가천대").name("이준희").build())
                .build();
        memberRepository.save(completedMember);
        memberVerifyRepository.save(MemberVerify.builder()
                .memberId(completedMember.getId())
                .contents("링크링크")
                .verifyStatus(SUCCESS)
                .verifyType(OPEN_KAKAO_ROOM_URL)
                .build());
        PageRequest pageRequest = PageRequest.of(0, 10);

        // when
        AdminMemberKakaoRoomResponses adminMemberKakaoRoomResponses = adminMemberService.readKakaoRoomPendingMembers(pageRequest);

        // then
        assertThat(adminMemberKakaoRoomResponses.getData()).hasSize(2);
    }

    @Test
    void 프로필_이미지_승인_대기중인_회원들을_조회할_수_있다() {
        // given
        Member pendingMember = memberRepository.save(Member.builder()
                .memberType(ADMIN)
                .memberStatus(APPROVAL)
                .oauthEmail("bookbla@bookbla.com")
                .memberProfile(MemberProfile.builder().profileImageStatus(ProfileImageStatus.PENDING).name("이준희").profileImageUrl("프사1").phoneNumber("01012345678").openKakaoRoomUrl("비밀링크").gender(MALE).schoolName("가천대").build())
                .build());
        memberVerifyRepository.save(MemberVerify.builder()
                .memberId(pendingMember.getId())
                .verifyStatus(PENDING)
                .verifyType(PROFILE_IMAGE)
                .contents("사진 링크~")
                .build());

        Member pendingMember2 = memberRepository.save(Member.builder()
                .memberType(ADMIN)
                .memberStatus(APPROVAL)
                .oauthEmail("bookbla@bookbla.com")
                .memberProfile(MemberProfile.builder().profileImageStatus(ProfileImageStatus.PENDING).name("문성진").profileImageUrl("프사2").phoneNumber("01012345678").openKakaoRoomUrl("비밀링크").gender(MALE).schoolName("가천대").build())
                .build());
        memberVerifyRepository.save(MemberVerify.builder()
                .memberId(pendingMember2.getId())
                .verifyStatus(PENDING)
                .verifyType(PROFILE_IMAGE)
                .contents("사진 링크~")
                .build());

        Member completedMember = memberRepository.save(Member.builder()
                .memberType(ADMIN)
                .memberStatus(APPROVAL)
                .oauthEmail("bookbla@bookbla.com")
                .memberProfile(MemberProfile.builder().profileImageStatus(ProfileImageStatus.PENDING).name("고도현").profileImageUrl("프사3").phoneNumber("01012345678").openKakaoRoomUrl("비밀링크").gender(MALE).schoolName("가천대").build())
                .build());
        memberVerifyRepository.save(MemberVerify.builder()
                .memberId(completedMember.getId())
                .verifyStatus(SUCCESS)
                .verifyType(PROFILE_IMAGE)
                .contents("사진 링크~")
                .build());
        PageRequest pageRequest = PageRequest.of(0, 10);

        // when
        AdminMemberProfileImageResponses adminMemberProfileImageResponses = adminMemberService.readProfileImagePendingMembers(pageRequest);

        // then
        assertThat(adminMemberProfileImageResponses.getData()).hasSize(2);
    }

    @Test
    void 학생증_승인_대기중인_회원_목록을_조회할_수_있다() {
        // given
        Member savedMember1 = memberRepository.save(Member.builder().build());
        memberVerifyRepository.save(MemberVerify.builder()
                .verifyType(STUDENT_ID)
                .verifyStatus(MemberVerifyStatus.PENDING)
                .memberId(savedMember1.getId())
                .contents("학생증 사진이 등록된 링크")
                .description("이름: 이길여, 학교: 서울대학교, 학과: 의학과, 학번: 19590000")
                .build());

        Member savedMember2 = memberRepository.save(Member.builder().build());
        memberVerifyRepository.save(MemberVerify.builder()
                .verifyType(STUDENT_ID)
                .verifyStatus(MemberVerifyStatus.PENDING)
                .memberId(savedMember2.getId())
                .contents("학생증 사진이 등록된 링크")
                .description("이름: 고도현, 학교: 가천대학교, 학과: 관광경영학과, 학번: 201900001")
                .build());

        Member savedMember3 = memberRepository.save(Member.builder().build());
        memberVerifyRepository.save(MemberVerify.builder()
                .verifyType(OPEN_KAKAO_ROOM_URL)
                .verifyStatus(MemberVerifyStatus.PENDING)
                .memberId(savedMember3.getId())
                .contents("카톡방 링크")
                .description("이것은 카톡방이여요~")
                .build());
        // 학생증 대기 2, 카톡방 대기 1
        PageRequest pageRequest = PageRequest.of(0, 10);

        // when
        AdminMemberStudentIdResponses adminMemberStudentIdResponses = adminMemberService.readStudentIdImagePendingMembers(pageRequest);

        // then
        assertThat(adminMemberStudentIdResponses.getData()).hasSize(2);
    }

    @AfterEach
    void tearDown() {
        memberRepository.deleteAll();
        memberVerifyRepository.deleteAll();
    }
}
