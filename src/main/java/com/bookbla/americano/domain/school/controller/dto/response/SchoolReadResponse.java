package com.bookbla.americano.domain.school.controller.dto.response;

import com.bookbla.americano.domain.school.repository.entity.School;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class SchoolReadResponse {

    private final List<SchoolDetail> schools;

    @Getter
    @Builder
    public static class SchoolDetail {
        private final Long id;
        private final String name;
        private final String emailDomain;
        private final String schoolStatus;
    }

    public static SchoolReadResponse from(List<School> schools) {
        List<SchoolDetail> schoolDetails = schools.stream()
            .map(school -> SchoolDetail.builder()
                .id(school.getId())
                .name(school.getName())
                .emailDomain(school.getEmailDomain())
                .schoolStatus(school.getSchoolStatus().toString())
                .build())
            .collect(Collectors.toList());

        return SchoolReadResponse.builder()
            .schools(schoolDetails)
            .build();
    }
}