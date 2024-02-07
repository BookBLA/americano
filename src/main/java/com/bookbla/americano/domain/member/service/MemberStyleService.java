package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.domain.member.controller.dto.MemberStylesResponse;
import com.bookbla.americano.domain.member.enums.ContactType;
import com.bookbla.americano.domain.member.enums.DateCostType;
import com.bookbla.americano.domain.member.enums.DateStyle;
import com.bookbla.americano.domain.member.enums.DrinkType;
import com.bookbla.americano.domain.member.enums.JustFriendType;
import com.bookbla.americano.domain.member.enums.SmokeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberStyleService {

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
