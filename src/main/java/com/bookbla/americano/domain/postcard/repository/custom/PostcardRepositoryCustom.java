package com.bookbla.americano.domain.postcard.repository.custom;

import com.bookbla.americano.domain.postcard.service.dto.response.PostcardFromResponse;
import com.bookbla.americano.domain.postcard.service.dto.response.PostcardToResponse;

import java.util.List;

public interface PostcardRepositoryCustom {
    List<PostcardFromResponse> getPostcardsFromMember(Long memberId);

    List<PostcardToResponse> getPostcardsToMember(Long memberId);
}
