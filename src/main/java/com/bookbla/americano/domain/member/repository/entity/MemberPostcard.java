package com.bookbla.americano.domain.member.repository.entity;

import com.bookbla.americano.base.entity.BaseUpdateEntity;
import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.postcard.enums.PostcardPayType;
import com.bookbla.americano.domain.postcard.exception.PostcardExceptionType;
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
public class MemberPostcard extends BaseUpdateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column
    @Builder.Default
    private int payPostcardCount = 10;

    @Column
    @Builder.Default
    private int freePostcardCount = 1;

    public void use(PostcardPayType payType) {
        validate(payType);
        if (payType == PostcardPayType.FREE) {
            freePostcardCount -= 1;
        } else {
            payPostcardCount -= 1;
        }
    }

    public void validate(PostcardPayType payType) {
        if (payType == PostcardPayType.FREE) {
            validateFreePostcardCount();
        } else {
            validatePayPostcardCount();
        }
    }

    private void validateFreePostcardCount() {
        if (freePostcardCount <= 0) {
            throw new BaseException(PostcardExceptionType.POSTCARD_TYPE_NOT_VALID);
        }
    }

    private void validatePayPostcardCount() {
        if (payPostcardCount <= 0) {
            throw new BaseException(PostcardExceptionType.POSTCARD_TYPE_NOT_VALID);
        }
    }
}
