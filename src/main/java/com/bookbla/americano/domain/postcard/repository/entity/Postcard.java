package com.bookbla.americano.domain.postcard.repository.entity;

import com.bookbla.americano.base.entity.BaseEntity;
import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import com.bookbla.americano.domain.postcard.enums.PostcardStatus;
import com.bookbla.americano.domain.postcard.exception.PostcardExceptionType;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.bookbla.americano.domain.postcard.enums.PostcardStatus.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
        @Index(name = "FK_SendMember", columnList = "send_member_id"),
        @Index(name = "FK_ReceiveMember", columnList = "receive_member_id"),
        @Index(name = "FK_ReceiveMemberBook", columnList = "receive_member_book_id"),
        @Index(name = "FK_PostcardType", columnList = "postcard_type_id")
})
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
    @JoinColumn(name = "receive_member_book_id")
    private MemberBook receiveMemberBook;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postcard_type_id")
    private PostcardType postcardType;

    private String imageUrl;

    private String message;

    @Enumerated(EnumType.STRING)
    private PostcardStatus postcardStatus;

    private LocalDateTime postcardStatusRefusedAt;

    private String channelUrl;

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

    public void validatePostcardStatusRefusedAt() {
        if (postcardStatusRefusedAt == null) {
            throw new BaseException(PostcardExceptionType.POSTCARD_STATUS_REFUSED_AT_NOT_SAVED);
        }
    }

    public Postcard updatePostcardStatusRefusedAt() { // 일단 PostcardStatus.REFUSED 인 경우입니다.
        this.postcardStatusRefusedAt = LocalDateTime.now();
        return this;
    }

    public boolean isPending() {
        return postcardStatus.isPending();
    }

    public boolean isRefused() {
        return postcardStatus.isRefused();
    }
}
