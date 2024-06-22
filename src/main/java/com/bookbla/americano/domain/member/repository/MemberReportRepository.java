package com.bookbla.americano.domain.member.repository;

import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberReport;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberReportRepository extends JpaRepository<MemberReport, Long> {

    Optional<MemberReport> findById(Long memberReportId);

    Optional<MemberReport> findByReporterMemberAndReportedByMember(Member reporterMember, Member reportedByMember);

    List<MemberReport> findAllByReporterMember(Member reporterMember);

    List<MemberReport> findAllByReportedByMember(Member repotedByMember);

}
