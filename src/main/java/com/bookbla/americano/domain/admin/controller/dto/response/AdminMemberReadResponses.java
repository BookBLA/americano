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

    private static final String NOT_REGISTERED = "등록되지 않았습니다";

    private final long totalCount;
    private final List<AdminMemberReadResponse> data;

    public static AdminMemberReadResponses from(long totalCount, List<Member> members) {
        List<AdminMemberReadResponse> adminMemberReadResponses = members.stream()
                .map(AdminMemberReadResponse::from)
                .collect(Collectors.toList());

        return new AdminMemberReadResponses(totalCount, adminMemberReadResponses);
    }


    @AllArgsConstructor
    @Builder
    @Getter
    public static class AdminMemberReadResponse {

        private final Long memberId;
        private final String authEmail;
        private final String memberType;
        private final String name;
        private final LocalDate birthDate;
        private final String schoolEmail;
        private final String gender;
        private final String school;
        private final String status;

        public static AdminMemberReadResponse from(Member member) {
            if (member.hasProfile()) {
                MemberProfile memberProfile = member.getMemberProfile();
                return AdminMemberReadResponse.builder()
                        .memberId(member.getId())
                        .authEmail(member.getOauthEmail())
                        .memberType(member.getMemberType().name())
                        .name(memberProfile.getName())
                        .birthDate(memberProfile.getBirthDate())
                        .schoolEmail(memberProfile.getSchoolEmail())
                        .gender(memberProfile.getGenderName())
                        .school(member.getSchool().getName())
                        .status(member.getMemberStatus().name())
                        .build();
            }

            return AdminMemberReadResponse.builder()
                    .memberId(member.getId())
                    .authEmail(member.getOauthEmail())
                    .memberType(member.getMemberType().name())
                    .name(NOT_REGISTERED)
                    .birthDate(LocalDate.now())
                    .schoolEmail(NOT_REGISTERED)
                    .gender(NOT_REGISTERED)
                    .school(NOT_REGISTERED)
                    .build();
        }
    }
}
