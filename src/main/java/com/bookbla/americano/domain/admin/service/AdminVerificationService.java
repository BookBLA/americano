package com.bookbla.americano.domain.admin.service;

import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.bookbla.americano.domain.member.enums.StudentIdImageStatus;
import com.bookbla.americano.domain.notification.enums.PushAlarmForm;
import com.bookbla.americano.domain.notification.service.AlarmService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import com.bookbla.americano.base.utils.ConvertUtil;
import com.bookbla.americano.domain.admin.service.dto.StatusUpdateDto;
import com.bookbla.americano.domain.member.enums.Gender;
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
    private final AlarmService alarmService;

    public void updateMemberStudentIdStatus(StatusUpdateDto dto) {
        MemberVerify memberVerify = memberVerifyRepository.getByIdOrThrow(dto.getMemberVerifyId());
        Member member = memberRepository.getByIdOrThrow(memberVerify.getMemberId());
        MemberProfile memberProfile = member.getMemberProfile();
        StudentIdImageStatus status = StudentIdImageStatus.from(dto.getStatus());

        updateVerification(dto, status, memberVerify, member, memberProfile);
        memberProfile.updateStudentIdImageStatus(status);

//        checkMemberStatuses(member, memberProfile);
    }

    private void updateVerification(
            StatusUpdateDto dto, StudentIdImageStatus status,
            MemberVerify memberVerify, Member member, MemberProfile memberProfile
    ) {
        if (status.isDone()) {
            updateMemberProfileByStudentIdElements(memberVerify, memberProfile);
            memberVerify.success(dto.getReason());
            alarmService.sendPushAlarm(member, PushAlarmForm.ADMIN_STUDENT_ID_IMAGE_ACCEPT);
            return;
        }
        if (status.isPending()) {
            return;
        }
        memberVerify.fail(dto.getReason());
        alarmService.sendPushAlarm(member, PushAlarmForm.ADMIN_STUDENT_ID_IMAGE_REJECT);
    }

    private void updateMemberProfileByStudentIdElements(MemberVerify memberVerify, MemberProfile memberProfile) {
        Map<String, String> descriptions = ConvertUtil.stringToMap(memberVerify.getDescription());
        memberProfile.updateStudentIdImageUrl(memberVerify.getContents())
                .updateGender(Gender.from(descriptions.getOrDefault("gender", DESCRIPTION_PARSING_FAIL)))
                .updateName(descriptions.getOrDefault("name", DESCRIPTION_PARSING_FAIL))
                .updateBirthDate(LocalDate.parse(descriptions.getOrDefault("birthDate", DESCRIPTION_PARSING_FAIL), DATE_FORMAT));
    }

    private void checkMemberStatuses(Member member, MemberProfile memberProfile) {
        if (memberProfile.isCertified() && member.getMemberStatus() == MemberStatus.APPROVAL) {
            member.updateMemberStatus(MemberStatus.STYLE, LocalDateTime.now());
            alarmService.sendPushAlarm(member, PushAlarmForm.ADMIN_VERIFICATION_ACCEPT);
        }
    }

}
