package com.bookbla.americano.domain.payment.repository.entity;

import com.bookbla.americano.base.entity.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
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
public class PaymentNotification extends BaseEntity { // 애플 공지 저장해두는 Entity

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    @Column(columnDefinition = "TEXT")
    private String information; // 서버로부터 받는 토큰 정보

    private String receipt; // 거래 후 받은 영수증 정보

    private String productId;

    public boolean isRefund() {
        return type.equalsIgnoreCase("REFUND");
    }

}
