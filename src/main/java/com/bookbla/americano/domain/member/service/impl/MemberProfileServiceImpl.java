package com.bookbla.americano.domain.member.service.impl;

import com.bookbla.americano.domain.member.controller.dto.response.MemberCreateResponse;
import com.bookbla.americano.domain.member.repository.MemberProfileRepository;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import com.bookbla.americano.domain.member.service.MemberProfileService;
import com.bookbla.americano.domain.member.service.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberProfileServiceImpl implements MemberProfileService {

    private final MemberProfileRepository memberProfileRepository;

    @Override
    public MemberCreateResponse createProfile(MemberDto memberDto) {
        MemberProfile memberProfile = memberProfileRepository.save(memberDto.toMemberProfileEntity());
        return MemberCreateResponse.from(memberProfile);
    }

}
