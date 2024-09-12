package com.bookbla.americano.domain.postcard.repository.entity;

import static com.bookbla.americano.domain.postcard.enums.PostcardStatus.ACCEPT;
import static com.bookbla.americano.domain.postcard.enums.PostcardStatus.ALL_WRONG;
import static com.bookbla.americano.domain.postcard.enums.PostcardStatus.PENDING;
import static com.bookbla.americano.domain.postcard.enums.PostcardStatus.READ;

import com.bookbla.americano.base.entity.BaseEntity;
import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.postcard.enums.PostcardStatus;
import com.bookbla.americano.domain.postcard.exception.PostcardExceptionType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Postcard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "send_member_id")
    private Member sendMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receive_member_id")
    private Member receiveMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postcard_type_id")
    private PostcardType postcardType;

    private String imageUrl;

    private String message;

    @Enumerated(EnumType.STRING)
    private PostcardStatus postcardStatus;

    private LocalDateTime postcardStatusRefusedAt;

    public void validateSendPostcard() {
        if (postcardStatus == READ) {
            throw new BaseException(PostcardExceptionType.PENDING_POSTCARD_EXISTS);
        }
        if (postcardStatus == PENDING) {
            throw new BaseException(PostcardExceptionType.PENDING_POSTCARD_EXISTS);
        }
        if (postcardStatus == ACCEPT) {
            throw new BaseException(PostcardExceptionType.ACCEPTED_POSTCARD_EXISTS);
        }
        if (postcardStatus == ALL_WRONG) {
            throw new BaseException(PostcardExceptionType.ALL_WRONG_POSTCARD_EXISTS);
        }
    }

    public void updatePostcardStatusRefusedAt() { // 일단 PostcardStatus.REFUSED 인 경우입니다.
        this.postcardStatusRefusedAt = LocalDateTime.now();
    }

    public boolean isPending() {
        return postcardStatus.isPending();
    }

    public boolean isRefused() {
        return postcardStatus.isRefused();
    }
}
