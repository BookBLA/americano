package com.bookbla.americano.domain.admin.controller.docs;

import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberKakaoRoomResponses;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberProfileImageResponses;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberProfileStatusResponse;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberReadResponses;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberStudentIdResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "관리자 회원 조회", description = "회원 조회용 API 모음입니다.")
public interface AdminMemberControllerDocs {

    @Operation(summary = "회원 전체 조회 API")
    @ApiResponse(
            responseCode = "200",
            description = "존재하는 모든 회원 목록을 반환합니다"
    )
    @GetMapping
    ResponseEntity<AdminMemberReadResponses> readMembers(Pageable pageable);

    @Operation(summary = "회원 프로필의 상태 목록 조회 API")
    @ApiResponse(
            responseCode = "200",
            description = "변경 요청시 필요한 회원 프로필들의 상태들을 반환합니다"
    )
    @GetMapping
    ResponseEntity<AdminMemberProfileStatusResponse> getMemberStatuses();

    @Operation(summary = "탈퇴 회원 목록 조회 API")
    @ApiResponse(
            responseCode = "200",
            description = "탈퇴한 회원 목록을 조회합니다."
    )
    @GetMapping
    ResponseEntity<AdminMemberReadResponses> readDeletedMembers(Pageable pageable);

    @Operation(summary = "카카오톡 오픈 승인 채팅방 대기 회원 조회 API")
    @ApiResponse(
            responseCode = "200",
            description = "카카오톡 승인 대기중인 회원 목록을 조회합니다"
    )
    @GetMapping
    ResponseEntity<AdminMemberKakaoRoomResponses> readKakaoOpenRoomUrlPendingMembers(Pageable pageable);

    @Operation(summary = "프로필 이미지 승인 대기 회원 조회 API")
    @ApiResponse(
            responseCode = "200",
            description = "프로필 승인 대기중인 회원 목록을 조회합니다."
    )
    @GetMapping
    ResponseEntity<AdminMemberProfileImageResponses> readProfileImagePendingMembers(Pageable pageable);

    @Operation(summary = "학생증 승인 대기 회원 조회 API")
    @ApiResponse(
            responseCode = "200",
            description = "학생증 승인 대기중인 회원 목록을 조회합니다."
    )
    @GetMapping
    ResponseEntity<AdminMemberStudentIdResponses> readStudentIdPendingMembers(Pageable pageable);

}
