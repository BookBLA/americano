package com.bookbla.americano.domain.admin.service;

import com.bookbla.americano.base.utils.ConvertUtil;
import com.bookbla.americano.domain.member.enums.StudentIdImageStatus;
import com.bookbla.americano.domain.notification.enums.PushAlarmForm;
import com.bookbla.americano.domain.notification.service.AlarmService;
import com.bookbla.americano.domain.admin.service.dto.StatusUpdateDto;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.MemberVerifyRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import com.bookbla.americano.domain.member.repository.entity.MemberVerify;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class AdminVerificationService {

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
    }

    private void updateVerification(
            StatusUpdateDto dto, StudentIdImageStatus status,
            MemberVerify memberVerify, Member member, MemberProfile memberProfile
    ) {
        if (status.isDone()) {
            updateMemberProfileByStudentIdImageUrl(memberVerify, memberProfile);
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

    private void updateMemberProfileByStudentIdImageUrl(MemberVerify memberVerify, MemberProfile memberProfile) {
        memberProfile.updateStudentIdImageUrl(memberVerify.getContents());
    }
}
