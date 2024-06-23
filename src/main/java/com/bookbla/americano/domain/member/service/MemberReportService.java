package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.domain.member.controller.dto.request.MemberReportCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberReportCreateResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberReportDeleteResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberReportReadResponse;

public interface MemberReportService {

    MemberReportCreateResponse addMemberReport(Long memberId, MemberReportCreateRequest memberReportCreateRequest);

    MemberReportReadResponse readMemberReport(Long memberId);

    MemberReportDeleteResponse deleteMemberReport(Long memberId, Long memberReportId);
}
