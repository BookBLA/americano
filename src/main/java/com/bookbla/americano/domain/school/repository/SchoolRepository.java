package com.bookbla.americano.domain.school.repository;

import com.bookbla.americano.domain.school.repository.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Long> {

    School findByName(String name);
}
