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
public enum DateCostType {

    DUTCH_PAY("더치페이", "💸더치페이가 좋아요"),
    ROTATE("번갈아가면서 사기", "👫번갈아가면서 사는게 좋아요"),
    MORE_MONEY("여유 있는 사람이 좀 더", "👑여유있는 사람이 좀 더 부담하는게 좋아요"),
    DATE_ACCOUNT("데이트 통장", "👩‍❤️‍👨데이트 통장을 만드는게 좋아요"),
    ;

    private final String value;
    private final String detailValue;

    public static DateCostType from(String dateCostType) {
        return Arrays.stream(values())
                .filter(it -> it.value.equalsIgnoreCase(dateCostType))
                .findFirst()
                .orElseThrow(() -> new BaseException(MemberExceptionType.DATE_COST_TYPE_NOT_VALID));
    }

    public static List<String> getValues() {
        return Arrays.stream(values())
                .map(it -> it.value)
                .collect(Collectors.toList());
    }
}
