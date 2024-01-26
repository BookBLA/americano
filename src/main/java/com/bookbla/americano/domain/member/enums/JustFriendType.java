package com.bookbla.americano.domain.member.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JustFriendType {
    ONE("절대 안됨"),
    TWO("단둘이 밥 먹기"),
    THREE("단둘이 카페가기"),
    FOUR("단둘이 공부하기"),
    FIVE("단둘이 영화보기"),
    SIX("단둘이 노래방가기"),
    SEVEN("단둘이 술마시기"),
    EIGHT("단둘이 여행가기"),
    NINE("상관없음"),
    ;

    private final String value;
}
