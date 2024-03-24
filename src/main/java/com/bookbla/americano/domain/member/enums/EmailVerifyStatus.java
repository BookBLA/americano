package com.bookbla.americano.domain.member.enums;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.exception.MailExceptionType;
import java.util.Arrays;

public enum EmailVerifyStatus {
    PENDING,
    DONE
    ;

    public static EmailVerifyStatus from(String name) {
        return Arrays.stream(values())
            .filter(it -> it.name().equalsIgnoreCase(name))
            .findFirst()
            .orElseThrow(() -> new BaseException(MailExceptionType.MAIL_VERIFY_STATUS_NOT_VALID));
    }

}
