package com.bookbla.americano.domain.sendbird.service;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
import com.bookbla.americano.domain.member.repository.MemberBookmarkRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBookmark;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ChatServiceTest {

    @Autowired
    private ChatService sut;

    @Autowired
    private MemberBookmarkRepository memberBookmarkRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 채팅을_수락한_회원은_책갈피가_30개_소모된다(){
        //given
        Member member = memberRepository.save(Member.builder().build());
        MemberBookmark womanMemberBookmark = memberBookmarkRepository.save(MemberBookmark.builder().member(member).bookmarkCount(40).build());

        //when
        sut.chatAccept(member.getId());

        //then
        MemberBookmark targetMemberBookmark = memberBookmarkRepository.findById(womanMemberBookmark.getId())
                .orElseThrow(() -> new BaseException(MemberExceptionType.EMPTY_MEMBER_BOOKMARK_INFO));
        assertThat(targetMemberBookmark.getBookmarkCount()).isEqualTo(10);
    }
    @Test
    void 책갈피가_개수가_부족하면_채팅을_수락할_수_없다(){
        //given
        Member targetmember = memberRepository.save(Member.builder().build());
        memberBookmarkRepository.save(MemberBookmark.builder().member(targetmember).bookmarkCount(10).build());

        // when & then
        assertThatThrownBy(() -> sut.chatAccept(targetmember.getId()))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("책갈피 개수가 부족합니다.");
    }

    @AfterEach
    void tearDown() {
        memberBookmarkRepository.deleteAll();
    }
}