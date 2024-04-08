package com.bookbla.americano.domain.auth.service.dto;

import com.bookbla.americano.domain.auth.repository.entity.MemberSignUpInformation;

public interface OAuth2MemberResponse {

    String getEmail();

    MemberSignUpInformation toMemberSignUpInformation();
}
