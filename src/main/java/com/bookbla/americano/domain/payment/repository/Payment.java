package com.bookbla.americano.domain.payment.repository;

import java.math.BigDecimal;

import com.bookbla.americano.base.entity.BaseEntity;
import com.bookbla.americano.domain.payment.enums.PaymentType;
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

    private String receipt;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    public void updateMemberId(Long memberId) {
        this.memberId = memberId;
    }
}
