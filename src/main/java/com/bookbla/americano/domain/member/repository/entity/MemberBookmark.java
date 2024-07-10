package com.bookbla.americano.domain.member.repository.entity;

import com.bookbla.americano.base.entity.BaseUpdateEntity;
import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.exception.MemberBookmarkExceptionType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberBookmark extends BaseUpdateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column
    @Builder.Default
    private int bookmarkCount = 0;

    public void use() {
        validate();
        bookmarkCount = bookmarkCount - 35;
    }

    public void validate() {
        if (bookmarkCount <= 35) {
            throw new BaseException(MemberBookmarkExceptionType.INVALID_BOOKMARK_COUNTS);
        }
    }

    // 엽서 -> 책갈피 변경시, 책갈피 150개
    public void addInvitationPostcard() {
        bookmarkCount += 150;
    }
}
