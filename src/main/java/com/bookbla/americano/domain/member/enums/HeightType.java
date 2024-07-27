package com.bookbla.americano.domain.member.enums;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static java.util.stream.Collectors.*;

@RequiredArgsConstructor
@Getter
public enum HeightType {

    LESS_THAN_150("150cm 미만", "150cm 미만"),
    OVER_150_AND_LESS_THAN_155("150cm 이상 ~ 155cm 미만", "150cm ~ 155cm"),
    OVER_155_AND_LESS_THAN_160("155cm 이상 ~ 160cm 미만", "155cm ~ 160cm"),
    OVER_160_AND_LESS_THAN_165("160cm 이상 ~ 165cm 미만", "160cm ~ 165cm"),
    OVER_165_AND_LESS_THAN_170("165cm 이상 ~ 170cm 미만", "165cm ~ 170cm"),
    OVER_170_AND_LESS_THAN_175("170cm 이상 ~ 175cm 미만", "170cm ~ 175cm"),
    OVER_175_AND_LESS_THAN_180("175cm 이상 ~ 180cm 미만", "175cm ~ 180cm"),
    OVER_180_AND_LESS_THAN_185("180cm 이상 ~ 185cm 미만", "180cm ~ 185cm"),
    OVER_185_AND_LESS_THAN_190("185cm 이상 ~ 190cm 미만", "185cm ~ 190cm"),
    OVER_190("190cm 이상 ~", "190cm 이상"),
    ;

    private final String value;
    private final String detailValue;

    public static HeightType from(String value) {
        return Arrays.stream(values())
                .filter(it -> it.getValue().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new BaseException(MemberExceptionType.HEIGHT_NOT_FOUND));
    }

    public static List<String> getValues() {
        return Arrays.stream(values())
                .map(HeightType::getValue)
                .collect(toList());
    }
}
