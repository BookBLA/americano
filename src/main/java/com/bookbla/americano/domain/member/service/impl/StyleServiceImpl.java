package com.bookbla.americano.domain.member.service.impl;

import com.bookbla.americano.domain.member.controller.dto.response.StylesResponse;
import com.bookbla.americano.domain.member.enums.ContactType;
import com.bookbla.americano.domain.member.enums.DateCostType;
import com.bookbla.americano.domain.member.enums.DateStyleType;
import com.bookbla.americano.domain.member.enums.DrinkType;
import com.bookbla.americano.domain.member.enums.JustFriendType;
import com.bookbla.americano.domain.member.enums.SmokeType;
import com.bookbla.americano.domain.member.service.StyleService;
import org.springframework.stereotype.Service;

@Service
public class StyleServiceImpl implements StyleService {

    @Override
    public StylesResponse readStyles() {
        return StylesResponse.of(
                SmokeType.getValues(),
                DrinkType.getValues(),
                ContactType.getValues(),
                DateStyleType.getValues(),
                DateCostType.getValues(),
                JustFriendType.getValues()
        );
    }

}
