package com.bookbla.americano.domain.member.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.MemberStatusLogRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import com.bookbla.americano.domain.member.repository.entity.MemberStatusLog;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberStatusLogRepository memberStatusLogRepository;


    @Test
    void 멤버_상태를_매칭_비활성화로_바꾸면_멤버_상태_변경_로그에_사유도_같이_저장된다() {
        // given
        Member member = memberRepository.save(
            Member.builder().memberProfile(MemberProfile.builder().name("테슷트").build()).build());

        // when
        memberService.updateStatus(member.getId(), MemberStatus.MATCHING_DISABLED, "그냥");

        // then
        Member savedMember = memberRepository.getByIdOrThrow(member.getId());
        MemberStatusLog savedMemberStatusLog = memberStatusLogRepository.getByMemberIdOrThrow(
            member.getId());

        assertThat(savedMember.getMemberStatus()).isEqualTo(MemberStatus.MATCHING_DISABLED);
        assertThat(savedMemberStatusLog.getAfterStatus()).isEqualTo(MemberStatus.MATCHING_DISABLED);
        assertThat(savedMemberStatusLog.getDescription()).isEqualTo("그냥");
    }

    @Test
    void 멤버_상태를_매칭_비활성화가_아닌_상태로_바꾸면_멤버_상태_변경_로그에_사유는_저장되지않는다() {
        // given
        Member member = memberRepository.save(
            Member.builder().memberProfile(MemberProfile.builder().name("테슷트").build()).build());

        // when
        memberService.updateStatus(member.getId(), MemberStatus.COMPLETED, "그냥");

        // then
        Member savedMember = memberRepository.getByIdOrThrow(member.getId());
        MemberStatusLog savedMemberStatusLog = memberStatusLogRepository.getByMemberIdOrThrow(
            member.getId());

        assertThat(savedMember.getMemberStatus()).isEqualTo(MemberStatus.COMPLETED);
        assertThat(savedMemberStatusLog.getAfterStatus()).isEqualTo(MemberStatus.COMPLETED);
        assertThat(savedMemberStatusLog.getDescription()).isNull();
    }

    @AfterEach
    void tearDown() {
        memberRepository.deleteAll();
        memberStatusLogRepository.deleteAll();
    }
}
