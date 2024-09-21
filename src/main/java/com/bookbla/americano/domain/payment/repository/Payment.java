package com.bookbla.americano.domain.payment.repository;

import java.math.BigDecimal;

import com.bookbla.americano.base.entity.BaseEntity;
import com.bookbla.americano.domain.member.repository.entity.MemberBookmark;
import com.bookbla.americano.domain.payment.enums.PaymentType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private BigDecimal money;

    private int bookmark;

    private String receipt; // 거래 후 받는 영수증 정보

    private String orderId; // 구글 인앱 결제 후 받는 주문 정보

    private String purchaseToken; // 구글 인앱 결제 후 받는 토큰 정보

    @Column(columnDefinition = "TEXT")
    private String information; // 서버로부터 받는 json 정보

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    public void updateMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public boolean canRefund(MemberBookmark memberBookmark) {
        return memberBookmark.getBookmarkCount() >= bookmark;
    }
}
