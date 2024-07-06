package com.bookbla.americano.domain.school.service;

import com.bookbla.americano.domain.member.enums.Gender;
import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import com.bookbla.americano.domain.school.controller.SchoolInvitationResponse;
import com.bookbla.americano.domain.school.repository.SchoolRepository;
import com.bookbla.americano.domain.school.repository.entity.School;
import com.bookbla.americano.domain.school.repository.entity.SchoolStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
class SchoolServiceTest {

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SchoolService schoolService;

    School school;

    @BeforeEach
    void setUp() {
        school = schoolRepository.save(School.builder()
                .name("가천대학교")
                .emailDomain("@gachon.ac.kr")
                .schoolStatus(SchoolStatus.OPEN)
                .build());
    }

    @Test
    void 사용자의_학교_등록_현황을_알_수_있다() {
        // given
        Member member = memberRepository.save(Member.builder()
                .memberProfile(MemberProfile.builder().gender(Gender.FEMALE).build())
                .memberStatus(MemberStatus.STYLE)
                .school(school)
                .invitationCode("마법의인비코드")
                .build());

        // when
        SchoolInvitationResponse schoolInformation = schoolService.getSchoolInformation(member.getId());

        // then
        assertAll(
                () -> assertThat(schoolInformation.getSchoolStatus()).isEqualTo("OPEN"),
                () -> assertThat(schoolInformation.getSchoolName()).isEqualTo("가천대학교"),
                () -> assertThat(schoolInformation.getInvitationCode()).isEqualTo("마법의인비코드"),
                () -> assertThat(schoolInformation.getCurrentMemberCount()).isOne(),
                () -> assertThat(schoolInformation.getGoalMemberCount()).isEqualTo(30),
                () -> assertThat(schoolInformation.getPercentage()).isEqualTo(3)
        );
    }

    @AfterEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
        schoolRepository.deleteAllInBatch();
    }
}
