package com.bookbla.americano.domain.postcard.repository;

import com.bookbla.americano.domain.postcard.repository.entity.PostcardType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostcardTypeRepository extends JpaRepository<PostcardType, Long> {
}
