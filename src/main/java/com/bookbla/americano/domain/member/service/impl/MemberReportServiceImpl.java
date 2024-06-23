package com.bookbla.americano.domain.member.service.impl;

import com.bookbla.americano.base.exception.BaseException;
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
import com.bookbla.americano.domain.member.service.MemberReportService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberReportServiceImpl implements MemberReportService {

    private final MemberRepository memberRepository;
    private final MemberReportRepository memberReportRepository;
    private final MemberStatusLogRepository memberStatusLogRepository;

    @Override
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
            .bookQuizReport(memberReportCreateRequest.getReportStatuses().getBookQuizReport())
            .reviewReport(memberReportCreateRequest.getReportStatuses().getReviewReport())
            .askReport(memberReportCreateRequest.getReportStatuses().getAskReport())
            .profileImageReport(memberReportCreateRequest.getReportStatuses().getProfileImageReport())
            .replyReport(memberReportCreateRequest.getReportStatuses().getReplyReport())
            .etcReport(memberReportCreateRequest.getReportStatuses().getEtcReport())
            .etcContents(memberReportCreateRequest.getEtcContents())
            .build();


        if (memberReport.hasAllReportsFalse()) {
            throw new BaseException(MemberReportExceptionType.ALL_REPORT_FALSE);
        }

        if (memberReport.isEtcReportWithoutContents()) {
            throw new BaseException(MemberReportExceptionType.ETC_CONTENTS_EMPTY);
        }

        memberReportRepository.save(memberReport);

        // 신고당한 횟수 늘리기
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

    @Override
    @Transactional(readOnly = true)
    public MemberReportReadResponse readMemberReport(Long memberId) {
        // 신고를 하는 멤버
        Member reporterMember = memberRepository.getByIdOrThrow(memberId);

        List<MemberReport> memberReports = memberReportRepository.findAllByReporterMember(reporterMember);
        return MemberReportReadResponse.from(memberReports);
    }

    @Override
    @Transactional
    public MemberReportDeleteResponse deleteMemberReport(Long memberId, Long memberReportId) {

        MemberReport memberReport = memberReportRepository.findById(memberReportId)
            .orElse(null);

        if (memberReport == null) {
            return MemberReportDeleteResponse.from(memberReportId);
        }

        // 신고를 하는 멤버
        Member reporterMember = memberRepository.getByIdOrThrow(memberId);

        if(!memberReport.getReporterMember().equals(reporterMember)) {
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
