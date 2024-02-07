package com.bookbla.americano.domain.member.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JustFriendType {

    NEVER("허용 X"),
    EATING("단 둘이 밥 먹기"),
    ALCOHOL("단 둘이 술 먹기"),
    TRAVEL("단 둘이 여행 가기"),
    EVERYTHING("상관 없음"),
    ;

    private final String value;

    public static List<String> getValues() {
        return Arrays.stream(values())
                .map(it -> it.value)
                .collect(Collectors.toList());
    }
}
