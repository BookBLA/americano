package com.bookbla.americano.domain.member.service;

import java.time.LocalDateTime;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.controller.dto.request.MemberInformationUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberDeleteResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberInformationReadResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberStatusResponse;
import com.bookbla.americano.domain.member.enums.Mbti;
import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.bookbla.americano.domain.member.enums.SmokeType;
import com.bookbla.americano.domain.member.exception.MemberBookmarkExceptionType;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
import com.bookbla.americano.domain.member.exception.MemberProfileException;
import com.bookbla.americano.domain.member.repository.MemberBookRepository;
import com.bookbla.americano.domain.member.repository.MemberBookmarkRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.MemberStatusLogRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBookmark;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import com.bookbla.americano.domain.member.repository.entity.MemberStatusLog;
import com.bookbla.americano.domain.member.repository.entity.MemberStyle;
import com.bookbla.americano.domain.school.repository.entity.School;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberBookRepository memberBookRepository;
    private final MemberRepository memberRepository;
    private final MemberStatusLogRepository memberStatusLogRepository;
    private final MemberBookmarkRepository memberBookmarkRepository;

    @Transactional(readOnly = true)
    public MemberResponse readMember(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        return MemberResponse.from(member);
    }

    @Transactional(readOnly = true)
    public MemberStatusResponse readMemberStatus(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        School school = member.getSchool();
        return MemberStatusResponse.from(member, school);
    }

    @Transactional
    public MemberDeleteResponse deleteMember(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);

        if (member.getMemberStatus() == MemberStatus.DELETED) {
            throw new BaseException(MemberExceptionType.MEMBER_STATUS_NOT_VALID);
        }
        memberStatusLogRepository.save(
                MemberStatusLog.builder()
                        .memberId(member.getId())
                        .beforeStatus(member.getMemberStatus())
                        .afterStatus(MemberStatus.DELETED)
                        .build()
        );
        member.updateDeleteAt(LocalDateTime.now())
                .updateMemberStatus(MemberStatus.DELETED, LocalDateTime.now());

        return MemberDeleteResponse.from(member);
    }

    @Transactional
    public MemberStatusResponse updateStatus(
            Long memberId,
            MemberStatus afterStatus
    ) {
        Member member = memberRepository.getByIdOrThrow(memberId);

        if (member.canChangeToComplete(afterStatus)) {
            int memberBooks = (int) memberBookRepository.countByMember(member);
            MemberBookmark memberBookmark = memberBookmarkRepository.findMemberBookmarkByMemberId(member.getId())
                    .orElseThrow(() -> new BaseException(MemberBookmarkExceptionType.MEMBER_ID_NOT_EXISTS));

            memberBookmark.updateBookmarksByInitialBook(memberBooks);
        }

        member.updateMemberStatus(afterStatus, LocalDateTime.now());
        School school = member.getSchool();
        return MemberStatusResponse.from(member, school);
    }

    @Transactional
    public void updateMemberInformation(Long memberId, MemberInformationUpdateRequest request) {
        Member member = memberRepository.getByIdOrThrow(memberId);

        validateName(member, request.getName());

        MemberProfile memberProfile = member.getMemberProfile();
        memberProfile.updateName(request.getName());

        MemberStyle memberStyle = member.getMemberStyle();
        member.updateMemberStyle(MemberStyle.builder()
                .profileImageType(memberStyle.getProfileImageType())
                .mbti(Mbti.from(request.getMbti()))
                .smokeType(SmokeType.from(request.getSmokeType()))
                .height(request.getHeight())
                .build());
    }

    private void validateName(Member member, String name) {
        if (name.equals(member.getMemberProfile().getName())) {
            return;
        }

        memberRepository.findByMemberProfileName(name)
                .ifPresent(profile -> {
                    throw new BaseException(MemberProfileException.ALREADY_EXISTS_NICKNAME);
                });
    }

    @Transactional(readOnly = true)
    public MemberInformationReadResponse readMemberInformation(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        return MemberInformationReadResponse.from(member);
    }
}
