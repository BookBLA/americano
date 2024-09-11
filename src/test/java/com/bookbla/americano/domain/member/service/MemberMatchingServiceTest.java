package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.domain.member.enums.Gender;
import com.bookbla.americano.domain.member.enums.StudentIdImageStatus;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import com.bookbla.americano.domain.member.service.dto.MemberProfileDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
class MemberMatchingServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberMatchingService memberMatchingService;

    @BeforeEach
    void setUp() {
        memberRepository.save(Member.builder().memberProfile(MemberProfile.builder().name("name 1").gender(Gender.FEMALE).studentIdImageStatus(StudentIdImageStatus.DONE).build()).build());
        memberRepository.save(Member.builder().memberProfile(MemberProfile.builder().name("name 2").gender(Gender.FEMALE).studentIdImageStatus(StudentIdImageStatus.DONE).build()).build());
        memberRepository.save(Member.builder().memberProfile(MemberProfile.builder().name("name 3").gender(Gender.FEMALE).studentIdImageStatus(StudentIdImageStatus.DONE).build()).build());
        memberRepository.save(Member.builder().memberProfile(MemberProfile.builder().name("name 4").gender(Gender.FEMALE).studentIdImageStatus(StudentIdImageStatus.DONE).build()).build());
        memberRepository.save(Member.builder().memberProfile(MemberProfile.builder().name("name 5").gender(Gender.MALE).build()).build());
        memberRepository.save(Member.builder().memberProfile(MemberProfile.builder().name("name 6").gender(Gender.MALE).build()).build());
        memberRepository.save(Member.builder().memberProfile(MemberProfile.builder().name("name 7").gender(Gender.MALE).build()).build());
    }

    @Test
    void 랜덤으로_4명의_사용자를_추천받을_수_있다() {
        // given
        Member member = memberRepository.save(Member.builder().build());
        member.updateMemberProfile(MemberProfile.builder().name("nickname").gender(Gender.MALE).build());
        memberRepository.save(member);

        // when
        List<Member> randomMatchingList = memberMatchingService.getRandomMatchingList(member.getId());
//        randomMatchingList.forEach(System.out::println);
        // then
        assertThat(randomMatchingList).hasSize(4);
        assertThat(randomMatchingList.get(0).getMemberProfile().getGenderName()).isEqualTo(Gender.FEMALE.name());
        assertThat(randomMatchingList.get(1).getMemberProfile().getGenderName()).isEqualTo(Gender.FEMALE.name());
        assertThat(randomMatchingList.get(2).getMemberProfile().getGenderName()).isEqualTo(Gender.FEMALE.name());
        assertThat(randomMatchingList.get(3).getMemberProfile().getGenderName()).isEqualTo(Gender.FEMALE.name());
//        assertThat(randomMatchingList.get(0).getMemberProfile().getName()).isEqualTo("name 1");
    }
}