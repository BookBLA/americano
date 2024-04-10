package com.bookbla.americano.domain.admin.service;

import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberAuthResponses;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberReadResponses;
import com.bookbla.americano.domain.member.enums.Gender;
import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.bookbla.americano.domain.member.enums.MemberType;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberAuth;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import static com.bookbla.americano.domain.member.enums.Gender.*;
import static com.bookbla.americano.domain.member.enums.MemberStatus.*;
import static com.bookbla.americano.domain.member.enums.MemberType.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
class AdminMemberServiceTest {

    @Autowired
    private AdminMemberService adminMemberService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 회원들의_정보를_확인할_수_있다() {
        // given
        Member member1 = Member.builder()
                .memberType(ADMIN)
                .oauthEmail("bookbla@bookbla.com")
                .memberAuth(MemberAuth.builder().schoolEmail("email.com").build())
                .memberProfile(MemberProfile.builder().phoneNumber("01012345678").gender(MALE).schoolName("가천대").name("이준희").build())
                .build();
        memberRepository.save(member1);

        Member member2 = Member.builder()
                .memberType(ADMIN)
                .oauthEmail("bookbla@bookbla.com")
                .memberAuth(MemberAuth.builder().schoolEmail("email.com").build())
                .memberProfile(MemberProfile.builder().phoneNumber("01012345678").gender(MALE).schoolName("가천대").name("이준희").build())
                .build();
        memberRepository.save(member2);

        Member member3 = Member.builder()
                .memberType(ADMIN)
                .oauthEmail("bookbla@bookbla.com")
                .memberAuth(MemberAuth.builder().schoolEmail("email.com").build())
                .memberProfile(MemberProfile.builder().phoneNumber("01012345678").gender(MALE).schoolName("가천대").name("이준희").build())
                .build();
        memberRepository.save(member3);

        // when
        AdminMemberReadResponses adminMemberReadResponses = adminMemberService.readMembers(PageRequest.of(0, 10));

        // then
        assertThat(adminMemberReadResponses.getData()).hasSize(3);
    }


    @Test
    void 승인_대기중인_회원들을_확인할_수_있다() {
        // given
        Member approvalMember1 = Member.builder()
                .memberType(ADMIN)
                .memberStatus(APPROVAL)
                .oauthEmail("bookbla@bookbla.com")
                .memberAuth(MemberAuth.builder().schoolEmail("email.com").build())
                .memberProfile(MemberProfile.builder().name("이준희").profileImageUrl("프사1").phoneNumber("01012345678").openKakaoRoomUrl("비밀링크").gender(MALE).schoolName("가천대").name("이준희").build())
                .build();
        memberRepository.save(approvalMember1);

        Member approvalMember2 = Member.builder()
                .memberType(ADMIN)
                .memberStatus(APPROVAL)
                .oauthEmail("bookbla@bookbla.com")
                .memberAuth(MemberAuth.builder().schoolEmail("email.com").build())
                .memberProfile(MemberProfile.builder().name("김진호").profileImageUrl("프사2").phoneNumber("01012345678").openKakaoRoomUrl("비밀링크").gender(MALE).schoolName("가천대").name("이준희").build())
                .build();
        memberRepository.save(approvalMember2);

        Member completedMember = Member.builder()
                .memberType(ADMIN)
                .memberStatus(COMPLETED)
                .oauthEmail("bookbla@bookbla.com")
                .memberAuth(MemberAuth.builder().schoolEmail("email.com").build())
                .memberProfile(MemberProfile.builder().name("문성진").profileImageUrl("프사3").phoneNumber("01012345678").openKakaoRoomUrl("비밀링크").gender(MALE).schoolName("가천대").name("이준희").build())
                .build();
        memberRepository.save(completedMember);

        // when
        AdminMemberAuthResponses adminMemberAuthResponses = adminMemberService.readApprovalStatusMembers(PageRequest.of(0, 10));

        // then
        assertThat(adminMemberAuthResponses.getData()).hasSize(2);
    }

}
