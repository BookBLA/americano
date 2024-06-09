package com.bookbla.americano.domain.postcard.repository;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.postcard.exception.PostcardExceptionType;
import com.bookbla.americano.domain.postcard.repository.entity.PostcardType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostcardTypeRepository extends JpaRepository<PostcardType, Long> {

    default PostcardType getByIdOrThrow(Long postCardTypeId) {
        return findById(postCardTypeId)
                .orElseThrow(() -> new BaseException(PostcardExceptionType.POSTCARD_TYPE_NOT_VALID));
    }
}
