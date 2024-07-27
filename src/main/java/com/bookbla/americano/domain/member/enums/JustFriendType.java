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
public enum JustFriendType {

    NEVER("허용 X", "❌허용불가"),
    EATING("단둘이 밥 먹기", "🍱단 둘이 밥 먹는 것까지 허용"),
    ALCOHOL("단둘이 술 먹기", "🍻단 둘이 술마시는 것까지 허용"),
    TRAVEL("단둘이 여행 가기", "✈️단 둘이 여행가는 것까지 허용"),
    EVERYTHING("상관 없음", "✅상관 없음"),
    ;

    private final String value;
    private final String detailValue;

    public static List<String> getValues() {
        return Arrays.stream(values())
                .map(it -> it.value)
                .collect(Collectors.toList());
    }

    public static JustFriendType from(String name) {
        return Arrays.stream(values())
                .filter(it -> it.value.equals(name))
                .findFirst()
                .orElseThrow(
                        () -> new BaseException(MemberExceptionType.JUST_FRIEND_TYPE_NOT_VALID));
    }
}
