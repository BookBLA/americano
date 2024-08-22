package com.bookbla.americano.domain.member.service;

import java.util.List;

import com.bookbla.americano.domain.member.controller.dto.response.StylesResponse;
import com.bookbla.americano.domain.member.repository.ProfileImageTypeRepository;
import com.bookbla.americano.domain.member.repository.entity.ProfileImageType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class StyleService {

    private final ProfileImageTypeRepository profileImageTypeRepository;

    public StylesResponse readStyles() {
        List<ProfileImageType> profileImageTypes = profileImageTypeRepository.findAll();
        return StylesResponse.from(profileImageTypes);
    }
}
