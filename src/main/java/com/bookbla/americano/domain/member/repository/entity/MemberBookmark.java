package com.bookbla.americano.domain.member.repository.entity;

import com.bookbla.americano.base.entity.BaseEntity;
import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.exception.MemberBookmarkExceptionType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberBookmark extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column
    @Builder.Default
    private int bookmarkCount = 0;

    @Column
    @Builder.Default
    private Integer admobCount = 2;

    public void use() {
        validate();
        bookmarkCount = bookmarkCount - 35;
    }

    public void validate() {
        if (bookmarkCount < 35) {
            throw new BaseException(MemberBookmarkExceptionType.INVALID_BOOKMARK_COUNTS);
        }
    }

    public void addInvitationBookmark() {
        bookmarkCount += 70;
    }

    public void addBookmark(int count) {
        this.bookmarkCount += count;
    }

    public void updateInitialBookBookmarks(int memberBooks) {
        if (memberBooks >= 4) {
            this.bookmarkCount += 60;
            return;
        }

        int counts = memberBooks * 15;
        this.bookmarkCount += counts;
    }

    public void watchAdmob() {
        if (admobCount <= 0) {
            throw new BaseException(MemberBookmarkExceptionType.ADMOB_COUNT_NOT_VALID);
        }
        this.admobCount--;
        this.bookmarkCount += 2;
    }
}
