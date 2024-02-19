package com.bookbla.americano.domain.member.service.impl;

import com.bookbla.americano.domain.member.controller.dto.response.MemberProfileCreateResponse;
import com.bookbla.americano.domain.member.repository.MemberProfileRepository;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import com.bookbla.americano.domain.member.service.MemberProfileService;
import com.bookbla.americano.domain.member.service.dto.MemberProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberProfileServiceImpl implements MemberProfileService {

    private final MemberProfileRepository memberProfileRepository;

    @Override
    public MemberProfileCreateResponse createProfile(MemberProfileDto memberProfileDto) {
        MemberProfile memberProfile = memberProfileRepository.save(memberProfileDto.toEntity());
        return MemberProfileCreateResponse.from(memberProfile);
    }

}
