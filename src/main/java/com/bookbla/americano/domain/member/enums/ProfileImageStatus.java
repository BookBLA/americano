package com.bookbla.americano.domain.member.enums;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.exception.MemberProfileException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum ProfileImageStatus {

    PENDING, // 첫 등록
    DENIAL, // 거절 됨
    DONE, // 완료
    CHANGING, // 변경 대기
    ;

    public static ProfileImageStatus from(String name) {
        return Arrays.stream(values())
            .filter(it -> it.name().equalsIgnoreCase(name))
            .findFirst()
            .orElseThrow(() -> new BaseException(MemberProfileException.OKA_STATUS_NOT_VALID));
    }

    public static List<String> getValues() {
        return Arrays.stream(values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    public boolean isDone() {
        return this == DONE;
    }
}
