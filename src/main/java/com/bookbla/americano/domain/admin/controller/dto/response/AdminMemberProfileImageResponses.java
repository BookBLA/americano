package com.bookbla.americano.domain.admin.controller.dto.response;

import java.time.LocalDate;
import java.util.List;

import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import com.bookbla.americano.domain.member.repository.entity.MemberVerify;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor
@Getter
public class AdminMemberProfileImageResponses {

    private final List<AdminMemberProfileImageResponse> datas;

    public static AdminMemberProfileImageResponses from(List<Member> members) {
        List<AdminMemberProfileImageResponse> pendingMemberProfileResponses = members.stream()
                .map(it -> AdminMemberProfileImageResponse.from(it))
                .collect(toList());

        return new AdminMemberProfileImageResponses(pendingMemberProfileResponses);
    }

    @AllArgsConstructor
    @Builder
    @Getter
    static class AdminMemberProfileImageResponse {

        private final Long memberId;
        private final String name;
        private final LocalDate birthDate;
        private final String gender;
        private final String schoolName;
        private final String phoneNumber;
        private final String profileImageUrl;
        private final String profileImageStatus;

        public static AdminMemberProfileImageResponse from(Member member) {
            MemberProfile memberProfile = member.getMemberProfile();

//            String profileImageUrl = memberVerifies.stream()
//                    .map(MemberVerify::getValue)
//                    .filter(it -> it.equals(member.getId()))
//                    .findFirst()
//                    .orElse("존재하지 않아요~");

            return AdminMemberProfileImageResponse.builder()
                    .memberId(member.getId())
                    .name(memberProfile.getName())
                    .birthDate(memberProfile.getBirthDate())
                    .name(memberProfile.getName())
                    .gender(memberProfile.getGender().name())
                    .schoolName(memberProfile.getSchoolName())
                    .phoneNumber(memberProfile.getPhoneNumber())
                    .profileImageUrl(memberProfile.getProfileImageUrl())
                    .profileImageStatus(memberProfile.getProfileImageStatus().name())
                    .build();
        }
    }
}
