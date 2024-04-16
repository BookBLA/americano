package com.bookbla.americano.domain.admin.controller.dto.response;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import com.bookbla.americano.domain.member.repository.entity.MemberVerify;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class AdminMemberKakaoRoomResponses {

    private final List<AdminMemberKakaoRoomResponse> datas;

    public static AdminMemberKakaoRoomResponses from(List<Member> members) {
        List<AdminMemberKakaoRoomResponse> adminMemberKakaoRoomResponses = members.stream()
                .map(it -> AdminMemberKakaoRoomResponse.from(it))
                .collect(Collectors.toList());

        return new AdminMemberKakaoRoomResponses(adminMemberKakaoRoomResponses);
    }

    @AllArgsConstructor
    @Builder
    @Getter
    static class AdminMemberKakaoRoomResponse {

        private final Long memberId;
        private final String name;
        private final String openKakaoRoomUrl;
        private final String openKakaoRoomUrlStatus;

        public static AdminMemberKakaoRoomResponse from(Member member) {
            MemberProfile memberProfile = member.getMemberProfile();

//            String openKakaoRoomUrl = memberVerifies.stream()
//                    .map(MemberVerify::getValue)
//                    .filter(it -> it.equals(member.getId()))
//                    .findFirst()
//                    .orElse("존재하지 않아요~");

            return AdminMemberKakaoRoomResponse.builder()
                    .memberId(member.getId())
                    .name(memberProfile.getName())
                    .openKakaoRoomUrl(memberProfile.getOpenKakaoRoomUrl())
                    .openKakaoRoomUrlStatus(memberProfile.getOpenKakaoRoomStatus().name())
                    .build();
        }
    }
}
