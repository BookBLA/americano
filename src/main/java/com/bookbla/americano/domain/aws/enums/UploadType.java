package com.bookbla.americano.domain.aws.enums;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.aws.exception.AwsException;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UploadType {

    IMAGE("image"),
    CERTIFICATION("certification"),
    PROFILE("profile"),
    UPDATE_CERTIFICATION("update-certification"),
    UPDATE_PROFILE("update-profile");

    private final String type;

    public static UploadType from(String type) {
        return Arrays.stream(values())
                .filter(value -> value.getType().equalsIgnoreCase(type))
                .findFirst()
                .orElseThrow(() -> new BaseException(AwsException.UPLOAD_TYPE_NOT_VALID));
    }
}
