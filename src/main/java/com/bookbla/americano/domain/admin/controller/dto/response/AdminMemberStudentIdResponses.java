package com.bookbla.americano.domain.admin.controller.dto.response;

import java.util.List;
import java.util.stream.Collectors;

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

    public static AdminMemberStudentIdResponses from(List<Member> members, List<MemberVerify> memberVerifies) {
        List<AdminMemberStudentIdResponse> adminMemberStudentIdResponses = members.stream()
                .map(it -> AdminMemberStudentIdResponse.from(it, memberVerifies))
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
        private final String studentIdImageUrl;
        private final String studentIdImageUrlStatus;

        public static AdminMemberStudentIdResponse from(Member member, List<MemberVerify> memberVerifies) {
            MemberProfile memberProfile = member.getMemberProfile();

            String studentIdImageUrl = memberVerifies.stream()
                    .map(MemberVerify::getValue)
                    .filter(it -> it.equals(member.getId()))
                    .findFirst()
                    .orElse("링크가 존재하지 않아요~");

            return AdminMemberStudentIdResponse.builder()
                    .memberId(member.getId())
                    .name(memberProfile.getName())
                    .schoolName(memberProfile.getSchoolName())
                    .major(memberProfile.getMajor())
                    .studentIdImageUrl(studentIdImageUrl)
                    .studentIdImageUrlStatus(memberProfile.getStudentIdImageStatus().name())
                    .build();
        }
    }
}
