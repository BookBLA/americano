package com.bookbla.americano.domain.postcard.repository.custom;

import com.bookbla.americano.domain.postcard.enums.PostcardStatus;
import com.bookbla.americano.domain.postcard.repository.entity.Postcard;
import com.bookbla.americano.domain.postcard.service.dto.response.PostcardFromResponse;

import java.util.List;

public interface PostcardRepositoryCustom {

    List<PostcardFromResponse> getPostcardsFromMember(Long memberId);

    List<Long> findReceiveByIdsWithPostcardStatus(Long sendMemberId, PostcardStatus postcardStatus);

    List<Long> findReceiveByIdsRefused(Long sendMemberId, List<Postcard> postcards);
}
