package com.bookbla.americano.base.log.enums;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.base.log.exception.PushAlarmLogExceptionType;
import java.util.Arrays;

public enum PushAlarmType {
    EXPO
    ;

    public static PushAlarmType from(String name) {
        return Arrays.stream(values())
            .filter(it -> it.name().equalsIgnoreCase(name))
            .findFirst()
            .orElseThrow(() -> new BaseException(PushAlarmLogExceptionType.TYPE_NOT_VALID));
    }

}
