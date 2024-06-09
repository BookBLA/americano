package com.bookbla.americano.domain.member.repository.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberPolicy {

    // 광고 동의
    @Column(length = 1)
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean adAgreementPolicy;

    private LocalDateTime adAgreementAt;

    public MemberPolicy updateAdAgreementPolicy(Boolean adAgreementPolicy) {
        this.adAgreementPolicy = adAgreementPolicy;
        return this;
    }

    public MemberPolicy updateAdAgreementAt(LocalDateTime adAgreementAt) {
        this.adAgreementAt = adAgreementAt;
        return this;
    }
}
