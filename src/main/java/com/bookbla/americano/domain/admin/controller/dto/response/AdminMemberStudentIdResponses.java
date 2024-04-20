package com.bookbla.americano.domain.admin.controller.dto.response;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.bookbla.americano.base.utils.ConvertUtil;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import com.bookbla.americano.domain.member.repository.entity.MemberVerify;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AdminMemberStudentIdResponses {

    private final List<AdminMemberStudentIdResponse> datas;

    public static AdminMemberStudentIdResponses from(List<MemberVerify> memberVerifies) {
        List<AdminMemberStudentIdResponse> adminMemberStudentIdResponses = memberVerifies.stream()
                .map(AdminMemberStudentIdResponse::from)
                .collect(Collectors.toList());

        return new AdminMemberStudentIdResponses(adminMemberStudentIdResponses);
    }


    @AllArgsConstructor
    @Builder
    @Getter
    static class AdminMemberStudentIdResponse {

        private final Long memberId;
        private final String name;
        private final String schoolName;
        private final String major;
        private final String studentId;
        private final String studentIdImageUrl;

        public static AdminMemberStudentIdResponse from(MemberVerify memberVerify) {
            Map<String, String> descriptions = ConvertUtil.stringToMap(memberVerify.getDescription());

            return AdminMemberStudentIdResponse.builder()
                    .memberId(memberVerify.getMemberId())
                    .name(descriptions.get("name"))
                    .schoolName(descriptions.get("schoolName"))
                    .major(descriptions.get("major"))
                    .studentId("studetId")
                    .studentIdImageUrl(memberVerify.getValue())
                    .build();
        }
    }
}