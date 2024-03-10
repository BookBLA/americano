package com.bookbla.americano.domain.member;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.exception.MailExceptionType;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
import java.util.regex.Pattern;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class Email {

    private static final Pattern REGEX_EMAIL = Pattern.compile(
            "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$");

    private String value;

    public Email(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (!REGEX_EMAIL.matcher(value).matches()) {
            throw new BaseException(MailExceptionType.EMAIL_NOT_VALID);
        }
    }
}