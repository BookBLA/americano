package com.bookbla.americano.domain.member.enums;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberStatus {

    PROFILE("p", "회원 정보 입력이 필요"),
    APPROVAL("a", "회원 승인이 필요"),
    STYLE("s", "스타일 입력이 필요"),
    BOOK("bo", "서재 입력이 필요"),
    COMPLETED("c", "회원 가입 모두 완료"),
    DELETED("d", "회원 탈퇴 상태"),
    BLOCKED("bl", "차단된 회원"),
    MATCHING_DISABLED("mb", "매칭 비활성화 회원");

    private final String value;
    private final String description;

    public static List<String> getValues() {
        return Arrays.stream(values())
            .map(it -> it.value)
            .collect(Collectors.toList());
    }

    public static MemberStatus from(String name) {
        return Arrays.stream(values())
            .filter(it -> it.value.equalsIgnoreCase(name))
            .findFirst()
            .orElseThrow(() -> new BaseException(MemberExceptionType.MEMBER_STATUS_NOT_VALID));
    }

    @JsonCreator
    public static MemberStatus fromName(String name) {
        return Arrays.stream(values())
            .filter(it -> it.name().equalsIgnoreCase(name))
            .findFirst()
            .orElseThrow(() -> new BaseException(MemberExceptionType.MEMBER_STATUS_NOT_VALID));
    }

    public boolean isMatchingDisabled() {
        return this == MemberStatus.MATCHING_DISABLED;
    }

}
