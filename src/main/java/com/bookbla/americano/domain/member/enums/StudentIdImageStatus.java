package com.bookbla.americano.domain.member.enums;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.exception.MemberAuthExceptionType;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum StudentIdImageStatus {

    PENDING,
    DENIAL,
    DONE,
    ;

    public static StudentIdImageStatus from(String name) {
        return Arrays.stream(values())
            .filter(it -> it.name().equalsIgnoreCase(name))
            .findFirst()
            .orElseThrow(() -> new BaseException(MemberAuthExceptionType.STATUS_IMAGE_ID_NOT_VALID));
    }

    public static List<String> getValues() {
        return Arrays.stream(values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    public boolean isDone() {
        return this == DONE;
    }

    public boolean isPending() {
        return this == PENDING;
    }
}