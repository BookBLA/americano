package com.bookbla.americano.domain.member.controller.dto.response;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.bookbla.americano.domain.member.enums.ProfileImageType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class StylesResponse {

    private List<ProfileImageResponse> profileImageResponseTypes;

    public static StylesResponse from(ProfileImageType[] values) {
        List<ProfileImageResponse> response = Arrays.stream(values)
                .map(it -> new ProfileImageResponse(it.getId(), it.getImageUrl()))
                .collect(Collectors.toList());
        return new StylesResponse(response);
    }

    @AllArgsConstructor
    public static class ProfileImageResponse {

        private int profileImageTypeId;
        private String profileImageUrl;

    }

}
