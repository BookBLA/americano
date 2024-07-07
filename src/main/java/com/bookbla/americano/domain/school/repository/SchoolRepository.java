package com.bookbla.americano.domain.school.repository;

import java.util.Optional;

import com.bookbla.americano.domain.school.repository.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Long> {

    Optional<School> findByEmailDomainAndName(String emailPostfix, String name);

    School findByName(String name);

}
