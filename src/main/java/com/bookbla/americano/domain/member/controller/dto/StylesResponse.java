package com.bookbla.americano.domain.member.controller.dto;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class StylesResponse {

    private final List<String> smokeTypes;
    private final List<String> drinkTypes;
    private final List<String> contactTypes;
    private final List<String> dateStyleTypes;
    private final List<String> dateCostTypes;
    private final List<String> justFriendTypes;

    public static StylesResponse of(
            List<String> smokeTypes,
            List<String> drinkTypes,
            List<String> contactTypes,
            List<String> dateStyleTypes,
            List<String> dateCostTypes,
            List<String> justFriendTypes
    ) {
        return new StylesResponse(smokeTypes, drinkTypes, contactTypes, dateStyleTypes,
                dateCostTypes, justFriendTypes);
    }

}
