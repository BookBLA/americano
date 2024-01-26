package com.bookbla.americano.domain.member.enums;

import javax.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public enum ContactType {
    SLOW, FAST
}
