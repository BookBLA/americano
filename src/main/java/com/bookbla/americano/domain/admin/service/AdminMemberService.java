package com.bookbla.americano.domain.admin.service;

import java.util.List;

import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberKakaoRoomResponses;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberProfileImageResponses;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberReadResponses;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberStudentIdResponses;
import com.bookbla.americano.domain.admin.service.dto.StatusUpdateDto;
import com.bookbla.americano.domain.member.enums.OpenKakaoRoomStatus;
import com.bookbla.americano.domain.member.enums.ProfileImageStatus;
import com.bookbla.americano.domain.member.enums.StudentIdImageStatus;
import com.bookbla.americano.domain.member.repository.MemberEmailRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.MemberVerifyRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Transactional
@Service
public class AdminMemberService {

    private final MemberRepository memberRepository;
    private final MemberVerifyRepository memberVerifyRepository;
    private final MemberEmailRepository memberEmailRepository;

    @Transactional(readOnly = true)
    public AdminMemberReadResponses readMembers(Pageable pageable) {
        Page<Member> memberPaging = memberRepository.findAll(pageable);
        List<Member> members = memberPaging.getContent();
        return AdminMemberReadResponses.from(members);
    }

    @Transactional(readOnly = true)
    public AdminMemberKakaoRoomResponses readKakaoRoomPendingMembers(Pageable pageable) {
//        List<MemberVerify> pendingVerifies = memberVerifyRepository.findByMemberVerifyStatusAndMemberVerifyType(MemberVerifyStatus.PENDING, OPEN_KAKAO_ROOM_URL);

        Page<Member> memberPaging = memberRepository.findByMemberProfileOpenKakaoRoomStatus(OpenKakaoRoomStatus.PENDING, pageable);

        List<Member> members = memberPaging.getContent();
        return AdminMemberKakaoRoomResponses.from(members);
    }

    public void updateMemberKakaoRoomStatus(StatusUpdateDto statusUpdateDto) {
        OpenKakaoRoomStatus openKakaoRoomStatus = OpenKakaoRoomStatus.from(statusUpdateDto.getStatus());

        Member member = memberRepository.getByIdOrThrow(statusUpdateDto.getMemberId());
        MemberProfile memberProfile = member.getMemberProfile();

        memberProfile.updateOpenKakaoRoomStatus(openKakaoRoomStatus);
        member.checkMemberStatus();
        // FCM 붙인 이후엔 성공/실패 푸시알림?
    }

    @Transactional(readOnly = true)
    public AdminMemberProfileImageResponses readProfileImagePendingMembers(Pageable pageable) {
//        List<MemberVerify> pendingVerifies = memberVerifyRepository.findByMemberVerifyStatusAndMemberVerifyType(MemberVerifyStatus.PENDING, PROFILE_IMAGE);

        Page<Member> memberPaging = memberRepository.findByMemberProfileProfileImageStatus(ProfileImageStatus.PENDING, pageable);
        List<Member> members = memberPaging.getContent();

        return AdminMemberProfileImageResponses.from(members);
    }

    public void updateMemberImageStatus(StatusUpdateDto statusUpdateDto) {
        ProfileImageStatus profileImageStatus = ProfileImageStatus.from(statusUpdateDto.getStatus());

        Member member = memberRepository.getByIdOrThrow(statusUpdateDto.getMemberId());
        MemberProfile memberProfile = member.getMemberProfile();

        memberProfile.updateProfileImageStatus(profileImageStatus);
        member.checkMemberStatus();
        // FCM 붙인 이후엔 성공/실패 푸시알림?
    }

    @Transactional(readOnly = true)
    public AdminMemberStudentIdResponses readStudentIdImagePendingMembers(Pageable pageable) {
//        List<MemberVerify> pendingVerifies = memberVerifyRepository.findByMemberVerifyStatusAndMemberVerifyType(MemberVerifyStatus.PENDING, STUDENT_ID);

        Page<Member> memberPaging = memberRepository.findByMemberProfileStudentIdImageStatus(StudentIdImageStatus.PENDING, pageable);
        List<Member> members = memberPaging.getContent();

        return AdminMemberStudentIdResponses.from(members);
    }

    public void updateMemberStudentIdStatus(StatusUpdateDto statusUpdateDto) {
        StudentIdImageStatus studentIdImageStatus = StudentIdImageStatus.from(statusUpdateDto.getStatus());

        Member member = memberRepository.getByIdOrThrow(statusUpdateDto.getMemberId());

        MemberProfile memberProfile = member.getMemberProfile();

        memberProfile.updateStudentIdImageStatus(studentIdImageStatus);
        member.checkMemberStatus();
        // FCM 붙인 이후엔 성공/실패 푸시알림?
    }
}
