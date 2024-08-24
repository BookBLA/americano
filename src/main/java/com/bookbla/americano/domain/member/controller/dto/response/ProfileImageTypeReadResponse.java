package com.bookbla.americano.domain.member.controller.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import com.bookbla.americano.domain.member.repository.entity.ProfileImageType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProfileImageTypeReadResponse {

    private List<ProfileImageResponse> profileImageResponseTypes;

    public static ProfileImageTypeReadResponse from(List<ProfileImageType> profileImageTypes) {
        List<ProfileImageResponse> response = profileImageTypes.stream()
                .map(it -> new ProfileImageResponse(it.getId(), it.getImageUrl()))
                .collect(Collectors.toList());
        return new ProfileImageTypeReadResponse(response);
    }

    @AllArgsConstructor
    public static class ProfileImageResponse {

        private Long profileImageTypeId;
        private String profileImageUrl;

    }
}
