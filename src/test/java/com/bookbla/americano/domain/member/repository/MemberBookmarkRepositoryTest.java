package com.bookbla.americano.domain.member.repository;

import com.bookbla.americano.base.config.JpaConfig;
import com.bookbla.americano.base.config.QuerydslConfig;
import com.bookbla.americano.domain.member.repository.entity.MemberBookmark;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static com.bookbla.americano.fixture.Fixture.MALE_MEMBER;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@Import({QuerydslConfig.class, JpaConfig.class})
@DataJpaTest
class MemberBookmarkRepositoryTest {

    @Autowired
    private MemberBookmarkRepository sut;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 모든_admobCount를_초기화_할_수_있다() {
        // given
        setUp();

        // when
        sut.resetAdmobCount(5);

        // then
        assertThat(sut.findAll().stream().allMatch(it -> it.getAdmobCount() == 5)).isTrue();
    }

    private void setUp() {
        sut.save(MemberBookmark.builder().admobCount(1).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(2).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(3).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(4).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(5).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(6).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(7).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(8).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(9).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(10).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(11).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(12).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(13).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(14).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(15).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(16).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(17).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(18).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(9999).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(9999).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(9999).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(9999).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(9999).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(9999).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(9999).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(9999).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(9999).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(9999).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(9999).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(9999).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(9999).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(9999).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(9999).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(9999).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(9999).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(9999).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(9999).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(9999).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(9999).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(9999).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(9999).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(9999).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(9999).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(9999).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(9999).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(9999).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(9999).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(9999).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(9999).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(9999).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(9999).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(9999).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(9999).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(9999).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(9999).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(9999).member(memberRepository.save(MALE_MEMBER)).build());
        sut.save(MemberBookmark.builder().admobCount(9999).member(memberRepository.save(MALE_MEMBER)).build());
    }
}
