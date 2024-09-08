package com.bookbla.americano.domain.member.repository;

import com.bookbla.americano.base.config.JpaConfig;
import com.bookbla.americano.base.config.QuerydslConfig;
import com.bookbla.americano.domain.member.repository.entity.MemberBookmark;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static com.bookbla.americano.fixture.Fixture.스타일_등록_완료_남성_고도리;
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

    @Autowired
    private MemberBookmarkRepository memberBookmarkRepository;

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
        for (int i = 0; i < 100; i++) {
            sut.save(MemberBookmark.builder()
                    .admobCount(i)
                    .member(memberRepository.save(스타일_등록_완료_남성_고도리))
                    .build());
        }
    }

    @AfterEach
    void tearDown() {
        memberBookmarkRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }
}
