package com.bookbla.americano.domain.member.enums;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
import java.util.Arrays;
import javax.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public enum MemberType {

    APPLE,
    KAKAO,
    GOOGLE,
    ADMIN,
    ;

    public boolean isAdmin() {
        return this == ADMIN;
    }

    public static MemberType findByName(String other) {
        return Arrays.stream(values())
                .filter(type -> other.equalsIgnoreCase(type.name()))
                .findFirst()
                .orElseThrow(() -> new BaseException(MemberExceptionType.MEMBER_TYPE_NOT_VALID));
    }
}
