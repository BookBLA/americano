package com.bookbla.americano.domain.school.service;

import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.school.controller.SchoolInvitationResponse;
import com.bookbla.americano.domain.school.repository.entity.School;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SchoolService {

    private final MemberRepository memberRepository;

    public SchoolInvitationResponse getSchoolInformation(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        School school = member.getSchool();
        return SchoolInvitationResponse.of(member, school);
    }
}
