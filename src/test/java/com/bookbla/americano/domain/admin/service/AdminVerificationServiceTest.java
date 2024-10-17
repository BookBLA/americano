package com.bookbla.americano.domain.admin.service;

import com.bookbla.americano.domain.admin.service.dto.StatusUpdateDto;
import com.bookbla.americano.domain.member.enums.MemberVerifyStatus;
import com.bookbla.americano.domain.member.enums.StudentIdImageStatus;
import com.bookbla.americano.domain.member.repository.MemberBookmarkRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.MemberVerifyRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBookmark;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import com.bookbla.americano.domain.member.repository.entity.MemberVerify;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.bookbla.americano.domain.member.enums.MemberStatus.APPROVAL;
import static com.bookbla.americano.domain.member.enums.MemberType.ADMIN;
import static com.bookbla.americano.domain.member.enums.MemberVerifyStatus.PENDING;
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

    @Autowired
    private MemberBookmarkRepository memberBookmarkRepository;

    @Autowired
    private AdminVerificationService sut;

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
        memberBookmarkRepository.save(MemberBookmark.builder()
            .member(member)
            .build());
        StatusUpdateDto statusUpdateDto = new StatusUpdateDto(memberVerify.getId(), "denial", "흐릿해요");

        // when
        sut.updateMemberStudentIdStatus(statusUpdateDto);

        // then
        MemberProfile memberProfile = memberRepository.getByIdOrThrow(member.getId()).getMemberProfile();
        MemberVerify findMemberVerify = memberVerifyRepository.getByIdOrThrow(memberVerify.getId());
        assertAll(
                () -> assertThat(memberProfile.getStudentIdImageStatus()).isEqualTo(StudentIdImageStatus.DENIAL),
                () -> assertThat(findMemberVerify.getVerifyStatus()).isEqualTo(MemberVerifyStatus.FAIL),
                () -> assertThat(findMemberVerify.getDescription()).isEqualTo("흐릿해요")
        );
    }

    @Test
    void 회원의_학생증_인증_성공시_재화_보상_지급한다() {
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
        memberBookmarkRepository.save(MemberBookmark.builder()
                .member(member)
                .build());

        StatusUpdateDto statusUpdateDto = new StatusUpdateDto(memberVerify.getId(), "done", "사진이 잘나오셨네요~");

        // when
        sut.updateMemberStudentIdStatus(statusUpdateDto);

        // then
        MemberProfile memberProfile = memberRepository.getByIdOrThrow(member.getId()).getMemberProfile();
        MemberVerify findMemberVerify = memberVerifyRepository.getByIdOrThrow(memberVerify.getId());
        MemberBookmark memberBookmark = memberBookmarkRepository.findMemberBookmarkByMemberId(member.getId()).orElseThrow();
        assertAll(
                () -> assertThat(memberBookmark.getBookmarkCount()).isEqualTo(35),
                () -> assertThat(memberProfile.getStudentIdImageStatus()).isEqualTo(StudentIdImageStatus.DONE),
                () -> assertThat(findMemberVerify.getVerifyStatus()).isEqualTo(MemberVerifyStatus.SUCCESS),
                () -> assertThat(findMemberVerify.getDescription()).isEqualTo("사진이 잘나오셨네요~")
        );
    }

    @AfterEach
    void tearDown() {
        memberBookmarkRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
        memberVerifyRepository.deleteAllInBatch();
    }
}
