package com.bookbla.americano.domain.member.controller.dto.request;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProfileModifyRequest {

    @NotNull
    private String openKakaoRoomUrl;

    @NotNull
    private String name;

    @NotNull
    private String phoneNumber;

    @NotNull
    private String schoolName;

}
