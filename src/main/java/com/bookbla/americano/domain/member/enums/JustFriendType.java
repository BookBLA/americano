package com.bookbla.americano.domain.member.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JustFriendType {

    NEVER("절대 안 됨"),
    EATING("단둘이 밥 먹기"),
    ALCOHOL("단둘이 술 먹기"),
    TRAVEL("단둘이 여행 가기"),
    EVERYTHING("상관 없음"),
    ;

    private final String value;
}