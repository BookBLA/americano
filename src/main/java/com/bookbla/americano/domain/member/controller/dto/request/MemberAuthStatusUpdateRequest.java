package com.bookbla.americano.domain.member.controller.dto.request;

import com.bookbla.americano.domain.member.enums.StudentIdImageStatus;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MemberAuthStatusUpdateRequest {

    @NotNull(message = "학생증이 인증 상태가 입력되지 않았습니다.")
    private String studentIdImageStatus;

    public StudentIdImageStatus getStudentIdImageStatus() {
        return StudentIdImageStatus.from(studentIdImageStatus);
    }
}
