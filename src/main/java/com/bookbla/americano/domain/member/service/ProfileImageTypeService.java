package com.bookbla.americano.domain.member.service;

import java.util.List;

import com.bookbla.americano.domain.member.controller.dto.response.ProfileImageTypeReadResponse;
import com.bookbla.americano.domain.member.repository.ProfileImageTypeRepository;
import com.bookbla.americano.domain.member.repository.entity.ProfileImageType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ProfileImageTypeService {

    private final ProfileImageTypeRepository profileImageTypeRepository;

    public ProfileImageTypeReadResponse readStyles() {
        List<ProfileImageType> profileImageTypes = profileImageTypeRepository.findAll();
        return ProfileImageTypeReadResponse.from(profileImageTypes);
    }
}
