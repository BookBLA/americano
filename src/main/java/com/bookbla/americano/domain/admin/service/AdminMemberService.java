package com.bookbla.americano.domain.admin.service;

import java.util.List;
import java.util.stream.Collectors;

import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberKakaoRoomResponses;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberProfileImageResponses;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberProfileStatusResponse;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberReadResponses;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberStudentIdResponses;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminPendingMemberResponses;
import com.bookbla.americano.domain.admin.service.dto.AlarmDto;
import com.bookbla.americano.domain.admin.service.dto.MemberStatusUpdateDto;
import com.bookbla.americano.domain.admin.service.dto.StatusUpdateDto;
import com.bookbla.americano.domain.alarm.service.AlarmClient;
import com.bookbla.americano.domain.alarm.service.AlarmService;
import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.bookbla.americano.domain.member.enums.OpenKakaoRoomStatus;
import com.bookbla.americano.domain.member.enums.ProfileImageStatus;
import com.bookbla.americano.domain.member.enums.StudentIdImageStatus;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.MemberVerifyRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import com.bookbla.americano.domain.member.repository.entity.MemberVerify;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.bookbla.americano.domain.member.enums.MemberVerifyStatus.PENDING;
import static com.bookbla.americano.domain.member.enums.MemberVerifyType.OPEN_KAKAO_ROOM_URL;
import static com.bookbla.americano.domain.member.enums.MemberVerifyType.PROFILE_IMAGE;
import static com.bookbla.americano.domain.member.enums.MemberVerifyType.STUDENT_ID;

@RequiredArgsConstructor
@Transactional
@Service
public class AdminMemberService {

    private final AlarmClient alarmClient;
    private final AlarmService alarmService;
    private final MemberRepository memberRepository;
    private final MemberVerifyRepository memberVerifyRepository;

    @Transactional(readOnly = true)
    public AdminMemberReadResponses readMembers(Pageable pageable) {
        long count = memberRepository.count();
        Page<Member> memberPaging = memberRepository.findAll(pageable);
        List<Member> members = memberPaging.getContent();
        return AdminMemberReadResponses.from(count, members);
    }

    @Transactional(readOnly = true)
    public AdminPendingMemberResponses readPendingMembers(Pageable pageable) {
        long count = memberRepository.countByMemberStatus(MemberStatus.APPROVAL);
        Page<Member> pendingMemberPaging = memberRepository.findByMemberStatus(MemberStatus.APPROVAL, pageable);
        List<Member> members = pendingMemberPaging.getContent();
        return AdminPendingMemberResponses.from(count, members);
    }

    public AdminMemberProfileStatusResponse readProfileStatuses() {
        return AdminMemberProfileStatusResponse.of(
                ProfileImageStatus.getValues(),
                OpenKakaoRoomStatus.getValues(),
                StudentIdImageStatus.getValues()
        );
    }

    public void updatePendingMemberStatus(MemberStatusUpdateDto dto) {
        Member member = memberRepository.getByIdOrThrow(dto.getMemberId());
        MemberProfile memberProfile = member.getMemberProfile();

        memberProfile.updateStudentIdImageStatus(dto.getStudentIdImageStatus())
                .updateProfileImageStatus(dto.getProfileImageStatus())
                .updateOpenKakaoRoomStatus(dto.getOpenKakaoRoomStatus());

        member.updateMemberStatus();
    }

    public void sendPushAlarm(AlarmDto alarmDto) {
        List<Member> members = memberRepository.findByMemberPolicyAdAgreementPolicy(true);
        List<Member> possibleMembers = members.stream()
                .filter(Member::canSendAdvertisementAlarm)
                .collect(Collectors.toList());

        possibleMembers.forEach(possibleMember ->
                alarmService.sendPushAlarm(possibleMember, alarmDto.getTitle(), alarmDto.getContents())
        );
    }

    @Transactional(readOnly = true)
    public AdminMemberReadResponses readDeletedMembers(Pageable pageable) {
        long count = memberRepository.countByMemberStatus(MemberStatus.DELETED);
        Page<Member> memberPaging = memberRepository.findByMemberStatus(MemberStatus.DELETED, pageable);
        List<Member> members = memberPaging.getContent();
        return AdminMemberReadResponses.from(count, members);
    }

    @Transactional(readOnly = true)
    public AdminMemberKakaoRoomResponses readKakaoRoomPendingMembers(Pageable pageable) {
        Page<MemberVerify> paging = memberVerifyRepository.findByVerifyTypeAndVerifyStatus(OPEN_KAKAO_ROOM_URL, PENDING, pageable);
        List<MemberVerify> memberVerifies = paging.getContent();
        return AdminMemberKakaoRoomResponses.from(memberVerifies);
    }

    public void updateMemberKakaoRoomStatus(StatusUpdateDto dto) {
        OpenKakaoRoomStatus status = OpenKakaoRoomStatus.from(dto.getStatus());

        MemberVerify memberVerify = memberVerifyRepository.getByIdOrThrow(dto.getMemberVerifyId());
        Member member = memberRepository.getByIdOrThrow(memberVerify.getMemberId());

        MemberProfile memberProfile = member.getMemberProfile();
        memberProfile.updateOpenKakaoRoomStatus(status);

        if (status.isDone()) {
            memberVerify.success();

            memberProfile.updateOpenKakaoRoomUrl(memberVerify.getContents());
        } else {
            memberVerify.fail(dto.getReason());
        }

        member.updateMemberStatus();
    }

    @Transactional(readOnly = true)
    public AdminMemberProfileImageResponses readProfileImagePendingMembers(Pageable pageable) {
        Page<MemberVerify> paging = memberVerifyRepository.findByVerifyTypeAndVerifyStatus(PROFILE_IMAGE, PENDING, pageable);
        List<MemberVerify> memberVerifies = paging.getContent();
        return AdminMemberProfileImageResponses.from(memberVerifies);
    }

    public void updateMemberImageStatus(StatusUpdateDto dto) {
        ProfileImageStatus status = ProfileImageStatus.from(dto.getStatus());

        MemberVerify memberVerify = memberVerifyRepository.getByIdOrThrow(dto.getMemberVerifyId());
        Member member = memberRepository.getByIdOrThrow(memberVerify.getMemberId());

        MemberProfile memberProfile = member.getMemberProfile();
        memberProfile.updateProfileImageStatus(status);

        if (status.isDone()) {
            memberVerify.success();

            memberProfile.updateProfileImageUrl(memberVerify.getContents());
        } else {
            memberVerify.fail(dto.getReason());
        }

        member.updateMemberStatus();
    }

    @Transactional(readOnly = true)
    public AdminMemberStudentIdResponses readStudentIdImagePendingMembers(Pageable pageable) {
        Page<MemberVerify> paging = memberVerifyRepository.findByVerifyTypeAndVerifyStatus(STUDENT_ID, PENDING, pageable);
        List<MemberVerify> memberVerifies = paging.getContent();
        return AdminMemberStudentIdResponses.from(memberVerifies);
    }

    public void updateMemberStudentIdStatus(StatusUpdateDto dto) {
        StudentIdImageStatus status = StudentIdImageStatus.from(dto.getStatus());

        MemberVerify memberVerify = memberVerifyRepository.getByIdOrThrow(dto.getMemberVerifyId());
        Member member = memberRepository.getByIdOrThrow(memberVerify.getMemberId());

        MemberProfile memberProfile = member.getMemberProfile();
        memberProfile.updateStudentIdImageStatus(status);

        if (status.isDone()) {
            memberVerify.success();

            memberProfile.updateStudentIdImageUrl(memberVerify.getContents());
        } else {
            memberVerify.fail(dto.getReason());
        }

        member.updateMemberStatus();
    }
}
