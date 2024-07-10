package com.bookbla.americano.domain.member.repository.entity;

import com.bookbla.americano.base.entity.BaseUpdateEntity;
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
public class MemberPostcard extends BaseUpdateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column
    @Builder.Default
    private int payPostcardCount = 8;

    @Column
    @Builder.Default
    private int freePostcardCount = 2;

    public void use() {
        if (freePostcardCount >= 1) {
            freePostcardCount--;
            return;
        }
        validate();
        payPostcardCount--;
    }

    public void validate() {
        if (freePostcardCount <= 0 && payPostcardCount <= 0) {
            throw new BaseException(MemberBookmarkExceptionType.INVALID_BOOKMARK_COUNTS);
        }
    }

    // 엽서 -> 책갈피 변경시, 책갈피 150개
    public void addInvitationPostcard() {
        payPostcardCount += 150;
    }
}
