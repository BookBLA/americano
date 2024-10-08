package com.bookbla.americano.domain.member.repository.entity;

import com.bookbla.americano.base.entity.BaseEntity;
import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.enums.AdmobType;
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

    public void sendPostcard() {
        validateSendPostcard();
        bookmarkCount = bookmarkCount - 35;
    }

    public void readPostcard() {
        validateReadPostcard();
        bookmarkCount = bookmarkCount - 5;
    }

    public void validateSendPostcard() {
        if (bookmarkCount < 35) {
            throw new BaseException(MemberBookmarkExceptionType.INVALID_BOOKMARK_COUNTS);
        }
    }

    public void validateReadPostcard() {
        if (bookmarkCount < 5) {
            throw new BaseException(MemberBookmarkExceptionType.INVALID_BOOKMARK_COUNTS);
        }
    }

    public void addWomanInvitationBookmark() {
        bookmarkCount += 70;
    }

    public void addManInvitationBookmark() {
        bookmarkCount += 35;
    }

    public void addBookmark(int count) {
        this.bookmarkCount += count;
    }

    public void refundBookmark(int count) {
        if (this.bookmarkCount < count) {
            throw new BaseException(MemberBookmarkExceptionType.INVALID_BOOKMARK_COUNTS);
        }
        this.bookmarkCount -= count;
    }

    public void updateBookmarksByInitialBook(int memberBooks) {
        if (memberBooks >= 4) {
            this.bookmarkCount += 60;
            return;
        }

        int counts = memberBooks * 15;
        this.bookmarkCount += counts;
    }

    public void watchAdmob(AdmobType admobType) {
        if (admobType == AdmobType.FREE_BOOKMARK) {
            watchBookmarkAdmob();
            return;
        }
        if (admobType == AdmobType.NEW_PERSON) {
            this.member.watchNewPersonAdmob();
        }
    }

    private void watchBookmarkAdmob() {
        this.member.watchBookmarkAdmob();
        this.bookmarkCount += 10;
    }

    public int getFreeBookmarkAdmobCount() {
        return member.getFreeBookmarkAdmobCount();
    }

    public int getNewPersonAdmobCount() {
        return member.getNewPersonAdmobCount();
    }
}
