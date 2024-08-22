package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.domain.member.controller.dto.response.StylesResponse;
import com.bookbla.americano.domain.member.enums.ProfileImageType;
import org.springframework.stereotype.Service;

@Service
public class StyleService {

    public StylesResponse readStyles() {
        return StylesResponse.from(ProfileImageType.values());
    }

}
