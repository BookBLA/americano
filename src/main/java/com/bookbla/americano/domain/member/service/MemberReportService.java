package com.bookbla.americano.domain.member.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.matching.exception.MemberMatchingExceptionType;
import com.bookbla.americano.domain.matching.repository.MatchExcludedRepository;
import com.bookbla.americano.domain.matching.repository.MemberMatchingRepository;
import com.bookbla.americano.domain.matching.repository.entity.MatchExcludedInfo;
import com.bookbla.americano.domain.matching.repository.entity.MemberMatching;
import com.bookbla.americano.domain.member.controller.dto.request.MemberReportCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberReportCreateResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberReportDeleteResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberReportReadResponse;
import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.bookbla.americano.domain.member.exception.MemberReportExceptionType;
import com.bookbla.americano.domain.member.repository.MemberReportRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.MemberStatusLogRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberReport;
import com.bookbla.americano.domain.member.repository.entity.MemberStatusLog;
import com.bookbla.americano.domain.member.service.dto.event.AdminNotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberReportService {

    private final MemberRepository memberRepository;
    private final MemberReportRepository memberReportRepository;
    private final MemberStatusLogRepository memberStatusLogRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final MatchExcludedRepository matchExcludedRepository;
    private final MemberMatchingRepository memberMatchingRepository;


    @Transactional
    public MemberReportCreateResponse addMemberReport(Long memberId,
                                                      MemberReportCreateRequest memberReportCreateRequest) {

        if (Objects.equals(memberId, memberReportCreateRequest.getReportedMemberId())) {
            throw new BaseException(MemberReportExceptionType.SAME_MEMBER);
        }

        // 신고를 하는 멤버
        Member reporterMember = memberRepository.getByIdOrThrow(memberId);

        // 신고를 당하는 멤버
        Member reportedMember = memberRepository.getByIdOrThrow(
                memberReportCreateRequest.getReportedMemberId());

        Optional<MemberReport> optionalMemberReport = memberReportRepository.findByReporterMemberAndReportedMember(reporterMember, reportedMember);

        if (optionalMemberReport.isPresent()) {
            return MemberReportCreateResponse.from(optionalMemberReport.get());
        }

        MemberReport memberReport = MemberReport.builder()
                .reporterMember(reporterMember)
                .reportedMember(reportedMember)
                .isNicknameReported(memberReportCreateRequest.getReportStatuses().getIsNicknameReported())
                .isBookQuizReported(memberReportCreateRequest.getReportStatuses().getIsBookQuizReported())
                .isReviewReported(memberReportCreateRequest.getReportStatuses().getIsReviewReported())
                .isConversationReported(memberReportCreateRequest.getReportStatuses().getIsConversationReported())
                .isProposalReported(memberReportCreateRequest.getReportStatuses().getIsProposalReported())
                .isOtherReported(memberReportCreateRequest.getReportStatuses().getIsOtherReported())
                .reportContents(memberReportCreateRequest.getReportContents())
                .build();


        if (memberReport.hasAllReportsFalse()) {
            throw new BaseException(MemberReportExceptionType.ALL_REPORT_FALSE);
        }

        memberReportRepository.save(memberReport);
        applicationEventPublisher.publishEvent(new AdminNotificationEvent("새 신고가 접수되었습니다", "신고당한 회원 id : " + reportedMember.getId().toString()));

        MemberMatching memberMatching = memberMatchingRepository.findByMember(reporterMember)
                .orElseThrow(() -> new BaseException(MemberMatchingExceptionType.MEMBER_MATCHING_NOT_FOUND));
        memberMatching.updateInvitationCard(true);

        // 신고 당한 회원 매칭 제외
        updateMemberMatchingExcluded(reporterMember, reportedMember);

        // 신고 당한 횟수 늘리기
        reportedMember.updateReportedCountUp();
        memberRepository.save(reportedMember);

        // 신고 횟수가 3회 이상인 유저는 MemberStatus 변경
        if (reportedMember.getReportedCount() >= 3) {

            // 멤버 이전 상태 기록
            memberStatusLogRepository.save(
                    MemberStatusLog.builder()
                            .memberId(reportedMember.getId())
                            .beforeStatus(reportedMember.getMemberStatus())
                            .afterStatus(MemberStatus.REPORTED)
                            .build()
            );

            reportedMember.updateMemberStatus(MemberStatus.REPORTED, LocalDateTime.now());
            memberRepository.save(reportedMember);
        }
        return MemberReportCreateResponse.from(memberReport);
    }

    private void updateMemberMatchingExcluded(Member sendMember, Member receiveMember) {
        matchExcludedRepository.findByMemberIdAndExcludedMemberId(sendMember.getId(), receiveMember.getId())
                .orElseGet(() -> matchExcludedRepository.save(MatchExcludedInfo.of(sendMember.getId(), receiveMember.getId())));

        matchExcludedRepository.findByMemberIdAndExcludedMemberId(receiveMember.getId(), sendMember.getId())
                .orElseGet(() -> matchExcludedRepository.save(MatchExcludedInfo.of(receiveMember.getId(), sendMember.getId())));
    }

    @Transactional(readOnly = true)
    public MemberReportReadResponse readMemberReport(Long memberId) {
        // 신고를 하는 멤버
        Member reporterMember = memberRepository.getByIdOrThrow(memberId);

        List<MemberReport> memberReports = memberReportRepository.findAllByReporterMember(reporterMember);
        return MemberReportReadResponse.from(memberReports);
    }

    @Transactional
    public MemberReportDeleteResponse deleteMemberReport(Long memberId, Long memberReportId) {

        MemberReport memberReport = memberReportRepository.findById(memberReportId)
                .orElse(null);

        if (memberReport == null) {
            return MemberReportDeleteResponse.from(memberReportId);
        }

        // 신고를 하는 멤버
        Member reporterMember = memberRepository.getByIdOrThrow(memberId);

        if (!memberReport.getReporterMember().equals(reporterMember)) {
            throw new BaseException(MemberReportExceptionType.NOT_SAME_MEMBER_REPORT);
        }

        // 신고를 당하는 멤버
        Member reportedMember = memberReport.getReportedMember();

        reportedMember.updateReportedCountDown();
        memberRepository.save(reportedMember);

        if (reportedMember.getReportedCount() < 3) {
            reportedMember.revertStatus(memberStatusLogRepository);
        }

        memberReportRepository.delete(memberReport);
        return MemberReportDeleteResponse.from(memberReportId);
    }
}
