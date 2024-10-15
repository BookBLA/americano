package com.bookbla.americano.domain.admin.service;

import java.time.LocalDateTime;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.admin.service.dto.StatusUpdateDto;
import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.bookbla.americano.domain.member.enums.StudentIdImageStatus;
import com.bookbla.americano.domain.member.exception.MemberBookmarkExceptionType;
import com.bookbla.americano.domain.member.repository.MemberBookmarkRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.MemberVerifyRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBookmark;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import com.bookbla.americano.domain.member.repository.entity.MemberVerify;
import com.bookbla.americano.domain.notification.enums.PushAlarmForm;
import com.bookbla.americano.domain.notification.event.PushAlarmEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class AdminVerificationService {

    private final MemberBookmarkRepository memberBookmarkRepository;
    private final MemberRepository memberRepository;
    private final MemberVerifyRepository memberVerifyRepository;
    private final PushAlarmEventHandler pushAlarmEventHandler;

    public void updateMemberStudentIdStatus(StatusUpdateDto dto) {
        MemberVerify memberVerify = memberVerifyRepository.getByIdOrThrow(dto.getMemberVerifyId());
        Member member = memberRepository.getByIdOrThrow(memberVerify.getMemberId());
        MemberProfile memberProfile = member.getMemberProfile();
        StudentIdImageStatus status = StudentIdImageStatus.from(dto.getStatus());

        updateVerification(dto, status, memberVerify, member, memberProfile);
        memberProfile.updateStudentIdImageStatus(status);

        checkInitialMemberApprove(member);
    }

    private void updateVerification(
            StatusUpdateDto dto, StudentIdImageStatus status,
            MemberVerify memberVerify, Member member, MemberProfile memberProfile
    ) {
        if (status.isDone()) {
            memberProfile.updateStudentIdImageUrl(memberVerify.getContents());
            memberVerify.success(dto.getReason());
            MemberBookmark memberBookmark = memberBookmarkRepository.findMemberBookmarkByMemberId(member.getId())
                    .orElseThrow(() -> new BaseException(MemberBookmarkExceptionType.MEMBER_ID_NOT_EXISTS));
            memberBookmark.addStudentIdCertificationReward();
            return;
        }
        if (status.isPending()) {
            return;
        }
        memberVerify.fail(dto.getReason());
        member.updateMemberStatus(MemberStatus.REJECTED, LocalDateTime.now());

        pushAlarmEventHandler.sendByAdmin(member, PushAlarmForm.ADMIN_STUDENT_ID_IMAGE_REJECT);
    }

    private void checkInitialMemberApprove(Member member) {
        if (member.getMemberStatus().equals(MemberStatus.APPROVAL)) {
            member.updateMemberStatus(MemberStatus.COMPLETED, LocalDateTime.now());
            pushAlarmEventHandler.sendByAdmin(member, PushAlarmForm.ADMIN_STUDENT_ID_IMAGE_REJECT);
        }
    }
}
