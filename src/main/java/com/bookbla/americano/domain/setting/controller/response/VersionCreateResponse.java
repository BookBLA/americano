package com.bookbla.americano.domain.setting.controller.response;

import com.bookbla.americano.domain.setting.repository.entity.Setting;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class VersionCreateResponse {

    private Long id;

    public static VersionCreateResponse from(Setting setting) {
        return VersionCreateResponse.builder()
            .id(setting.getId())
            .build();
    }
}
