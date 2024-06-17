package com.bookbla.americano.domain.admin.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import com.bookbla.americano.base.utils.ConvertUtil;
import com.bookbla.americano.domain.admin.service.dto.StatusUpdateDto;
import com.bookbla.americano.domain.member.enums.Gender;
import com.bookbla.americano.domain.member.enums.OpenKakaoRoomStatus;
import com.bookbla.americano.domain.member.enums.ProfileImageStatus;
import com.bookbla.americano.domain.member.enums.StudentIdImageStatus;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.MemberVerifyRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import com.bookbla.americano.domain.member.repository.entity.MemberVerify;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.bookbla.americano.domain.member.repository.entity.MemberVerify.DESCRIPTION_PARSING_FAIL;

@RequiredArgsConstructor
@Transactional
@Service
public class AdminVerificationService {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final MemberRepository memberRepository;
    private final MemberVerifyRepository memberVerifyRepository;

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
                .updateBirthDate(LocalDate.parse(descriptions.getOrDefault("birthDate", DESCRIPTION_PARSING_FAIL), DATE_FORMAT));
    }

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

}
