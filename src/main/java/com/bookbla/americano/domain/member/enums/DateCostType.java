package com.bookbla.americano.domain.member.enums;

import javax.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public enum DateCostType {
    DUTCH_PAY, ROTATE, MORE_MONEY, DATE_ACCOUNT
}
