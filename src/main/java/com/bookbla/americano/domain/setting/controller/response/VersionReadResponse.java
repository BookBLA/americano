package com.bookbla.americano.domain.setting.controller.response;

import com.bookbla.americano.domain.setting.repository.entity.Setting;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class VersionReadResponse {

    private String version;

    public static VersionReadResponse from(Setting setting) {
        return VersionReadResponse.builder()
            .version(setting.getVersion())
            .build();
    }
}
