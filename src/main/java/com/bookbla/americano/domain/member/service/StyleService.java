package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.domain.member.controller.dto.response.StylesResponse;
import com.bookbla.americano.domain.member.enums.ContactType;
import com.bookbla.americano.domain.member.enums.DateCostType;
import com.bookbla.americano.domain.member.enums.DateStyle;
import com.bookbla.americano.domain.member.enums.DrinkType;
import com.bookbla.americano.domain.member.enums.JustFriendType;
import com.bookbla.americano.domain.member.enums.SmokeType;
import org.springframework.stereotype.Service;

@Service
public class StyleService {

    public StylesResponse readStyles() {
        return StylesResponse.of(
                SmokeType.getValues(),
                DrinkType.getValues(),
                ContactType.getValues(),
                DateStyle.getValues(),
                DateCostType.getValues(),
                JustFriendType.getValues()
        );
    }
}
