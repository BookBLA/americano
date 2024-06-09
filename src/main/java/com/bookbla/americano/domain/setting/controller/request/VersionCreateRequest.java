package com.bookbla.americano.domain.setting.controller.request;

import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class VersionCreateRequest {

    @NotNull(message = "버전이 입력되지 않았습니다.")
    private String version;
}
