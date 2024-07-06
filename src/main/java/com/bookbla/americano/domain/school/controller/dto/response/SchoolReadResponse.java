package com.bookbla.americano.domain.school.controller.dto.response;

import com.bookbla.americano.domain.school.repository.entity.School;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SchoolReadResponse {

    private final List<SchoolDetail> schools;

    @Getter
    @Builder
    public static class SchoolDetail {
        private final Long id;
        private final String name;
        private final String url;
    }

    public static SchoolReadResponse from(List<School> schools) {
        List<SchoolDetail> schoolDetails = schools.stream()
            .map(school -> SchoolDetail.builder()
                .id(school.getId())
                .name(school.getName())
                .url(school.getUrl())
                .build())
            .collect(Collectors.toList());

        return SchoolReadResponse.builder()
            .schools(schoolDetails)
            .build();
    }
}
