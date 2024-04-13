package com.bookbla.americano.domain.aws.controller.enums;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.aws.exception.UploadTypeNotValidException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum UploadType {

    IMAGE("image"),
    CERTIFICATION("certification"),
    PROFILE("profile");

    private final String type;

    public static UploadType from(String type) {
        return Arrays.stream(values())
                .filter(value -> value.getType().equalsIgnoreCase(type))
                .findFirst()
                .orElseThrow(() -> new BaseException(UploadTypeNotValidException.UPLOAD_TYPE_NOT_VALID));
    }
}
