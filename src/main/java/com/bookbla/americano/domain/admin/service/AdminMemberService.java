package com.bookbla.americano.domain.admin.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import com.bookbla.americano.base.utils.ConvertUtil;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberKakaoRoomResponses;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberProfileImageResponses;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberProfileStatusResponse;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberReadResponses;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberStudentIdResponses;
import com.bookbla.americano.domain.admin.service.dto.StatusUpdateDto;
import com.bookbla.americano.domain.member.enums.Gender;
import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.bookbla.americano.domain.member.enums.MemberVerifyStatus;
import com.bookbla.americano.domain.member.enums.OpenKakaoRoomStatus;
import com.bookbla.americano.domain.member.enums.ProfileImageStatus;
import com.bookbla.americano.domain.member.enums.StudentIdImageStatus;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.MemberVerifyRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import com.bookbla.americano.domain.member.repository.entity.MemberVerify;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.bookbla.americano.domain.member.enums.MemberVerifyStatus.PENDING;
import static com.bookbla.americano.domain.member.enums.MemberVerifyType.OPEN_KAKAO_ROOM_URL;
import static com.bookbla.americano.domain.member.enums.MemberVerifyType.PROFILE_IMAGE;
import static com.bookbla.americano.domain.member.enums.MemberVerifyType.STUDENT_ID;
import static com.bookbla.americano.domain.member.repository.entity.MemberVerify.DESCRIPTION_PARSING_FAIL;

@RequiredArgsConstructor
@Service
@Slf4j
public class AdminMemberService {

    private final MemberRepository memberRepository;
    private final MemberVerifyRepository memberVerifyRepository;

    @Transactional(readOnly = true)
    public AdminMemberReadResponses readMembers(Pageable pageable) {
        long count = memberRepository.count();
        Page<Member> memberPaging = memberRepository.findAll(pageable);
        List<Member> members = memberPaging.getContent();
        return AdminMemberReadResponses.from(count, members);
    }

    public AdminMemberProfileStatusResponse readProfileStatuses() {
        return AdminMemberProfileStatusResponse.of(
                ProfileImageStatus.getValues(),
                OpenKakaoRoomStatus.getValues(),
                StudentIdImageStatus.getValues(),
                MemberVerifyStatus.getValues()
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
        long count = memberVerifyRepository.countByVerifyTypeAndVerifyStatus(OPEN_KAKAO_ROOM_URL, PENDING);
        Page<MemberVerify> paging = memberVerifyRepository.findByVerifyTypeAndVerifyStatus(OPEN_KAKAO_ROOM_URL, PENDING, pageable);
        List<MemberVerify> memberVerifies = paging.getContent();
        return AdminMemberKakaoRoomResponses.from(count, memberVerifies);
    }

    @Transactional
    public void updateMemberKakaoRoomStatus(StatusUpdateDto dto) {
        MemberVerify memberVerify = memberVerifyRepository.getByIdOrThrow(dto.getMemberVerifyId());
        Member member = memberRepository.getByIdOrThrow(memberVerify.getMemberId());
        MemberProfile memberProfile = member.getMemberProfile();
        OpenKakaoRoomStatus status = OpenKakaoRoomStatus.from(dto.getStatus());

        updateVerification(dto, status, memberVerify, memberProfile);

        memberProfile.updateOpenKakaoRoomStatus(status);
        member.updateMemberStatus();
    }

    private void updateVerification(
            StatusUpdateDto dto, OpenKakaoRoomStatus status,
            MemberVerify memberVerify, MemberProfile memberProfile
    ) {
        if (status.isDone()) {
            memberVerify.success(dto.getReason());
            memberProfile.updateOpenKakaoRoomUrl(memberVerify.getContents());
            return;
        }
        if (status.isPending()) {
            return;
        }
        memberVerify.fail(dto.getReason());
    }

    @Transactional(readOnly = true)
    public AdminMemberProfileImageResponses readProfileImagePendingMembers(Pageable pageable) {
        long count = memberVerifyRepository.countByVerifyTypeAndVerifyStatus(PROFILE_IMAGE, PENDING);
        Page<MemberVerify> paging = memberVerifyRepository.findByVerifyTypeAndVerifyStatus(PROFILE_IMAGE, PENDING, pageable);
        List<MemberVerify> memberVerifies = paging.getContent();
        return AdminMemberProfileImageResponses.from(count, memberVerifies);
    }

    @Transactional
    public void updateMemberImageStatus(StatusUpdateDto dto) {
        MemberVerify memberVerify = memberVerifyRepository.getByIdOrThrow(dto.getMemberVerifyId());
        Member member = memberRepository.getByIdOrThrow(memberVerify.getMemberId());
        MemberProfile memberProfile = member.getMemberProfile();
        ProfileImageStatus status = ProfileImageStatus.from(dto.getStatus());

        updateVerification(dto, status, memberVerify, memberProfile);

        memberProfile.updateProfileImageStatus(status);
        member.updateMemberStatus();
    }

    private void updateVerification(
            StatusUpdateDto dto, ProfileImageStatus status,
            MemberVerify memberVerify, MemberProfile memberProfile
    ) {
        if (status.isDone()) {
            memberProfile.updateProfileImageUrl(memberVerify.getContents());
            memberVerify.success(dto.getReason());
            return;
        }
        if (status.isPending()) {
            return;
        }
        memberVerify.fail(dto.getReason());
    }

    @Transactional(readOnly = true)
    public AdminMemberStudentIdResponses readStudentIdImagePendingMembers(Pageable pageable) {
        long count = memberVerifyRepository.countByVerifyTypeAndVerifyStatus(STUDENT_ID, PENDING);
        Page<MemberVerify> paging = memberVerifyRepository.findByVerifyTypeAndVerifyStatus(STUDENT_ID, PENDING, pageable);
        List<MemberVerify> memberVerifies = paging.getContent();
        return AdminMemberStudentIdResponses.from(count, memberVerifies);
    }

    @Transactional
    public void updateMemberStudentIdStatus(StatusUpdateDto dto) {
        MemberVerify memberVerify = memberVerifyRepository.getByIdOrThrow(dto.getMemberVerifyId());
        Member member = memberRepository.getByIdOrThrow(memberVerify.getMemberId());
        MemberProfile memberProfile = member.getMemberProfile();
        StudentIdImageStatus status = StudentIdImageStatus.from(dto.getStatus());

        updateVerification(dto, status, memberVerify, memberProfile);

        memberProfile.updateStudentIdImageStatus(status);
        member.updateMemberStatus();
    }

    private void updateVerification(
            StatusUpdateDto dto, StudentIdImageStatus status,
            MemberVerify memberVerify, MemberProfile memberProfile
    ) {
        if (status.isDone()) {
            updateMemberProfileByStudentIdElements(memberVerify, memberProfile);
            memberVerify.success(dto.getReason());
            return;
        }
        if (status.isPending()) {
            return;
        }
        memberVerify.fail(dto.getReason());
    }

    private void updateMemberProfileByStudentIdElements(MemberVerify memberVerify, MemberProfile memberProfile) {
        Map<String, String> descriptions = ConvertUtil.stringToMap(memberVerify.getDescription());
        memberProfile.updateStudentIdImageUrl(memberVerify.getContents())
                .updateGender(Gender.from(descriptions.getOrDefault("gender", DESCRIPTION_PARSING_FAIL)))
                .updateName(descriptions.getOrDefault("name", DESCRIPTION_PARSING_FAIL))
                .updateSchoolName(descriptions.getOrDefault("schoolName", DESCRIPTION_PARSING_FAIL))
                .updateBirthDate(LocalDate.parse(descriptions.getOrDefault("birthDate", DESCRIPTION_PARSING_FAIL), DateTimeFormatter.ofPattern("yyyyMMdd")));
    }
}
