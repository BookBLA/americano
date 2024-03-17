package com.bookbla.americano.domain.member.enums;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberStatus {

    PROFILE("p"),
    APPROVAL("a"),
    STYLE_BOOK("s"),
    COMPLETED("c"),
    ;

    private final String value;

    public static List<String> getValues() {
        return Arrays.stream(values())
            .map(it -> it.value)
            .collect(Collectors.toList());
    }

    public static MemberStatus from(String name) {
        return Arrays.stream(values())
            .filter(it -> it.name().equalsIgnoreCase(name))
            .findFirst()
            .orElseThrow(() -> new BaseException(MemberExceptionType.GENDER_NOT_VALID));
    }

}
