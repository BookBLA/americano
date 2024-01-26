package com.bookbla.americano.domain.member.enums;

import javax.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public enum Mbti {
    INTP,
    INTJ,
    INFP,
    INFJ,
    ISTP,
    ISTJ,
    ISFP,
    ISFJ,

    ENTP,
    ENTJ,
    ENFP,
    ENFJ,
    ESTP,
    ESTJ,
    ESFP,
    ESFJ
}
