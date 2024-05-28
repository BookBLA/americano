package com.bookbla.americano.domain.admin.controller.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import com.bookbla.americano.domain.member.repository.entity.MemberVerify;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class AdminMemberKakaoRoomResponses {

    private final long totalCount;
    private final List<AdminMemberKakaoRoomResponse> datas;

    public static AdminMemberKakaoRoomResponses from(long totalCount, List<MemberVerify> memberVerifies) {
        List<AdminMemberKakaoRoomResponse> adminMemberKakaoRoomResponses = memberVerifies.stream()
                .map(AdminMemberKakaoRoomResponse::from)
                .collect(Collectors.toList());

        return new AdminMemberKakaoRoomResponses(totalCount, adminMemberKakaoRoomResponses);
    }

    @RequiredArgsConstructor
    @Builder
    @Getter
    static class AdminMemberKakaoRoomResponse {

        private final Long memberVerifyId;
        private final Long memberId;
        private final String name;
        private final String openKakaoRoomUrl;

        public static AdminMemberKakaoRoomResponse from(MemberVerify memberVerify) {

            return AdminMemberKakaoRoomResponse.builder()
                    .memberVerifyId(memberVerify.getId())
                    .memberId(memberVerify.getMemberId())
                    .name(memberVerify.getDescription())
                    .openKakaoRoomUrl(memberVerify.getContents())
                    .build();
        }
    }
}