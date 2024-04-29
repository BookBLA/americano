package com.bookbla.americano.domain.admin.controller.dto.response;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@Getter
public class AdminMemberReadResponses {

    private final List<AdminMemberReadResponse> data;

    public static AdminMemberReadResponses from(List<Member> members) {
        List<AdminMemberReadResponse> adminMemberReadResponses = members.stream()
                .map(AdminMemberReadResponse::from)
                .collect(Collectors.toList());

        return new AdminMemberReadResponses(adminMemberReadResponses);
    }


    @AllArgsConstructor
    @Builder
    @Getter
    static class AdminMemberReadResponse {

        private final String name;
        private final LocalDate birthDate;
        private final String email;
        private final String gender;
        private final String school;
        private final String phone;

        public static AdminMemberReadResponse from(Member member) {
            MemberProfile memberProfile = member.getMemberProfile();

            return AdminMemberReadResponse.builder()
                    .name(memberProfile.getName())
                    .birthDate(memberProfile.getBirthDate())
                    .email(memberProfile.getSchoolEmail())
                    .gender(memberProfile.getGender().name())
                    .school(memberProfile.getSchoolName())
                    .phone(memberProfile.getPhoneNumber())
                    .build();
        }
    }
}
