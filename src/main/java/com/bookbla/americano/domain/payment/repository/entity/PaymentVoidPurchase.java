package com.bookbla.americano.domain.payment.repository.entity;

import com.bookbla.americano.base.entity.BaseEntity;
import com.bookbla.americano.domain.payment.enums.PaymentType;
import com.bookbla.americano.domain.payment.enums.VoidedReason;
import com.bookbla.americano.domain.payment.enums.VoidedSource;
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
@Getter
@Builder
@Entity
public class PaymentVoidPurchase extends BaseEntity { // 구글 환불 정보

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderId;

    private String purchaseToken;

    @Enumerated(EnumType.STRING)
    private VoidedSource voidedSource;

    @Enumerated(EnumType.STRING)
    private VoidedReason voidedReason;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

}
