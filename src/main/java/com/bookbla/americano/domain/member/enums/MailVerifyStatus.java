package com.bookbla.americano.domain.member.enums;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.exception.MailExceptionType;
import java.util.Arrays;

public enum MailVerifyStatus {
    PENDING,
    DONE
    ;

    public static MailVerifyStatus from(String name) {
        return Arrays.stream(values())
            .filter(it -> it.name().equalsIgnoreCase(name))
            .findFirst()
            .orElseThrow(() -> new BaseException(MailExceptionType.MAIL_VERIFY_STATUS_NOT_VALID));
    }

}
