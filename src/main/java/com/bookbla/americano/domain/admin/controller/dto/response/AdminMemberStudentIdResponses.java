package com.bookbla.americano.domain.admin.controller.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.bookbla.americano.base.utils.ConvertUtil;
import com.bookbla.americano.domain.member.repository.entity.MemberVerify;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.bookbla.americano.domain.member.repository.entity.MemberVerify.DESCRIPTION_PARSING_FAIL;

@RequiredArgsConstructor
@Getter
public class AdminMemberStudentIdResponses {

    private final long totalCount;
    private final List<AdminMemberStudentIdResponse> data;

    public static AdminMemberStudentIdResponses from(long totalCount, List<MemberVerify> memberVerifies) {
        List<AdminMemberStudentIdResponse> adminMemberStudentIdResponses = memberVerifies.stream()
                .map(AdminMemberStudentIdResponse::from)
                .collect(Collectors.toList());

        return new AdminMemberStudentIdResponses(totalCount, adminMemberStudentIdResponses);
    }


    @RequiredArgsConstructor
    @Builder
    @Getter
    static class AdminMemberStudentIdResponse {

        private final Long memberVerifyId;
        private final Long memberId;
        private final String name;
        private final String schoolName;
        private final String birthDate;
        private final String gender;
        private final String studentIdImageUrl;
        private final LocalDateTime createdAt;

        public static AdminMemberStudentIdResponse from(MemberVerify memberVerify) {
            Map<String, String> descriptions = ConvertUtil.stringToMap(memberVerify.getDescription());

            return AdminMemberStudentIdResponse.builder()
                    .memberVerifyId(memberVerify.getId())
                    .memberId(memberVerify.getMemberId())
                    .name(descriptions.getOrDefault("name", DESCRIPTION_PARSING_FAIL))
                    .schoolName(descriptions.getOrDefault("schoolName", DESCRIPTION_PARSING_FAIL))
                    .gender(descriptions.getOrDefault("gender", DESCRIPTION_PARSING_FAIL))
                    .birthDate(descriptions.getOrDefault("birthDate", DESCRIPTION_PARSING_FAIL))
                    .studentIdImageUrl(memberVerify.getContents())
                    .createdAt(memberVerify.getCreatedAt())
                    .build();
        }
    }
}