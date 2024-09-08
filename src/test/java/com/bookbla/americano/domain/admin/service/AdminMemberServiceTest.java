package com.bookbla.americano.domain.admin.service;

import java.time.LocalDate;

import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberReadResponses;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberStudentIdResponses;
import com.bookbla.americano.domain.member.enums.MemberVerifyStatus;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.MemberVerifyRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import com.bookbla.americano.domain.member.repository.entity.MemberVerify;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import static com.bookbla.americano.domain.member.enums.Gender.MALE;
import static com.bookbla.americano.domain.member.enums.MemberStatus.APPROVAL;
import static com.bookbla.americano.domain.member.enums.MemberStatus.COMPLETED;
import static com.bookbla.americano.domain.member.enums.MemberStatus.STYLE;
import static com.bookbla.americano.domain.member.enums.MemberType.ADMIN;
import static com.bookbla.americano.domain.member.enums.MemberVerifyType.OPEN_KAKAO_ROOM_URL;
import static com.bookbla.americano.domain.member.enums.MemberVerifyType.STUDENT_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
class AdminMemberServiceTest {

    @Autowired
    private AdminMemberService adminMemberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberVerifyRepository memberVerifyRepository;

    @Test
    @Disabled
        // github action NPE
    void 회원들의_정보를_확인할_수_있다() {
        // given
        Member member1 = Member.builder()
                .memberType(ADMIN)
                .memberStatus(COMPLETED)
                .oauthEmail("bookbla@bookbla.com")
                .memberProfile(MemberProfile.builder()
                        .birthDate(LocalDate.of(1999, 3, 3))
                        .gender(MALE)
                        .name("이준희")
                        .build()
                ).build();
        memberRepository.saveAndFlush(member1);

        Member member2 = Member.builder()
                .memberType(ADMIN)
                .memberStatus(STYLE)
                .oauthEmail("bookbla@bookbla.com")
                .memberProfile(MemberProfile.builder().birthDate(LocalDate.of(1999, 3, 3))
                        .gender(MALE).name("이준희").build())
                .build();
        memberRepository.saveAndFlush(member2);

        Member member3 = Member.builder()
                .memberType(ADMIN)
                .memberStatus(APPROVAL)
                .oauthEmail("bookbla@bookbla.com")
                .memberProfile(MemberProfile.builder().gender(MALE).name("이준희").build())
                .build();
        memberRepository.saveAndFlush(member3);

        // when
        AdminMemberReadResponses adminMemberReadResponses = adminMemberService.readMembers(PageRequest.of(0, 10));
        AdminMemberReadResponses.AdminMemberReadResponse adminMemberReadResponse = adminMemberReadResponses.getData()
                .get(0);

        // then
        assertAll(
                () -> assertThat(adminMemberReadResponses.getTotalCount()).isEqualTo(3),
                () -> assertThat(adminMemberReadResponses.getData()).hasSize(3),
                () -> assertThat(adminMemberReadResponse.getMemberType()).isEqualToIgnoringCase("ADMIN"),
                () -> assertThat(adminMemberReadResponse.getAuthEmail()).isEqualTo("bookbla@bookbla.com"),
                () -> assertThat(adminMemberReadResponse.getName()).isEqualTo("이준희"),
                () -> assertThat(adminMemberReadResponse.getGender()).isEqualToIgnoringCase("male"),
                () -> assertThat(adminMemberReadResponse.getBirthDate()).isEqualTo(LocalDate.of(1999, 3, 3))
        );
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

        // when
        PageRequest pageRequest = PageRequest.of(0, 10);
        AdminMemberStudentIdResponses adminMemberStudentIdResponses = adminMemberService.readStudentIdImagePendingMembers(
                pageRequest);

        // then
        assertThat(adminMemberStudentIdResponses.getData()).hasSize(2);
    }

    @AfterEach
    void tearDown() {
        memberVerifyRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }
}
