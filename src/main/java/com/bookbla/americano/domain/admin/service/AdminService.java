package com.bookbla.americano.domain.admin.service;

import java.awt.print.Pageable;
import java.util.List;

import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberProfileImageResponses;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberProfileKakaoRoomResponses;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberProfileStudentIdImageResponses;
import com.bookbla.americano.domain.admin.service.dto.StatusUpdateDto;
import com.bookbla.americano.domain.member.enums.OpenKakaoRoomStatus;
import com.bookbla.americano.domain.member.enums.ProfileImageStatus;
import com.bookbla.americano.domain.member.enums.StudentIdImageStatus;
import com.bookbla.americano.domain.member.repository.MemberProfileRepository;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class AdminService {

    private final MemberProfileRepository memberProfileRepository;

    @Transactional(readOnly = true)
    public AdminMemberProfileKakaoRoomResponses readOpenKakaoRoomPendingMemberProfiles(Pageable pageable) {
        List<MemberProfile> pendingMemberProfiles = memberProfileRepository.findByOpenKakaoRoomStatus(OpenKakaoRoomStatus.PENDING, pageable);
        return AdminMemberProfileKakaoRoomResponses.from(pendingMemberProfiles);
    }

    public void updateMemberProfileKakaoRoomStatus(StatusUpdateDto statusUpdateDto) {
        String status = statusUpdateDto.getStatus();
        OpenKakaoRoomStatus openKakaoRoomStatus = OpenKakaoRoomStatus.from(status);

        Long memberProfileId = statusUpdateDto.getMemberProfileId();
        MemberProfile memberProfile = memberProfileRepository.getByIdOrThrow(memberProfileId);

        memberProfile.updateOpenKakaoRoomStatus(openKakaoRoomStatus);
        // 성공 푸쉬알림?
    }

    @Transactional(readOnly = true)
    public AdminMemberProfileImageResponses readProfileImagePendingMemberProfiles(Pageable pageable) {
        List<MemberProfile> pendingMemberProfiles = memberProfileRepository.findByProfileImageStatus(ProfileImageStatus.PENDING, pageable);
        return AdminMemberProfileImageResponses.from(pendingMemberProfiles);
    }

    public void updateMemberProfileImageStatus(StatusUpdateDto statusUpdateDto) {
        String status = statusUpdateDto.getStatus();
        ProfileImageStatus profileImageStatus = ProfileImageStatus.from(status);

        Long memberProfileId = statusUpdateDto.getMemberProfileId();
        MemberProfile memberProfile = memberProfileRepository.getByIdOrThrow(memberProfileId);

        memberProfile.updateProfileImageStatus(profileImageStatus);
        // 성공 푸쉬알림?
    }

    @Transactional(readOnly = true)
    public AdminMemberProfileStudentIdImageResponses readStudentIdImagePendingMemberProfiles(Pageable pageable) {
        List<MemberProfile> pendingMemberProfiles = memberProfileRepository.findByStudentIdImageStatus(StudentIdImageStatus.PENDING, pageable);
        return AdminMemberProfileStudentIdImageResponses.from(pendingMemberProfiles);
    }

    public void updateMemberProfileStudentIdImageStatus(StatusUpdateDto statusUpdateDto) {
        String status = statusUpdateDto.getStatus();
        StudentIdImageStatus studentIdImageStatus = StudentIdImageStatus.from(status);

        Long memberProfileId = statusUpdateDto.getMemberProfileId();
        MemberProfile memberProfile = memberProfileRepository.getByIdOrThrow(memberProfileId);

        memberProfile.updateStudentIdImageStatus(studentIdImageStatus);
        // 성공 푸쉬알림?
    }

}
