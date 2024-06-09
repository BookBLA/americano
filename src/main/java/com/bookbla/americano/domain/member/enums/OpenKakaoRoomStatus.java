package com.bookbla.americano.domain.member.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.exception.MemberProfileException;

public enum OpenKakaoRoomStatus {

    PENDING, // 등록 대기
    INACCESSIBLE, // 접근 불가
    NOT_DEFAULT, // 기본 프로필 아님
    DONE, // 변경 대기
    ;

    public static OpenKakaoRoomStatus from(String name) {
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
