package com.bookbla.americano.domain.school.service;

import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.school.controller.dto.response.SchoolInvitationResponse;
import com.bookbla.americano.domain.school.controller.dto.response.SchoolReadResponse;
import com.bookbla.americano.domain.school.repository.SchoolRepository;
import com.bookbla.americano.domain.school.repository.entity.School;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SchoolService {

    private final MemberRepository memberRepository;
    private final SchoolRepository schoolRepository;

    public SchoolInvitationResponse getSchoolInformation(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        School school = member.getSchool();
        return SchoolInvitationResponse.of(member, school);
    }

    public SchoolReadResponse readSchool() {
        List<School> schools = schoolRepository.findAll();
        return SchoolReadResponse.from(schools);
    }
}
