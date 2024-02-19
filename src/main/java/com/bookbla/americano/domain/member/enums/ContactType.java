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
public enum ContactType {

    SLOW("느긋이"),
    FAST("칼답"),
    ;

    private final String value;

    public static List<String> getValues() {
        return Arrays.stream(values())
                .map(it -> it.value)
                .collect(Collectors.toList());
    }

    public static ContactType from(String name) {
        return Arrays.stream(values())
                .filter(it -> it.value.equals(name))
                .findFirst()
                .orElseThrow(() -> new BaseException(MemberExceptionType.CONTACT_TYPE_NOT_VALID));
    }
}
