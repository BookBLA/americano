package com.bookbla.americano.domain.auth.repository;

import com.bookbla.americano.domain.user.repository.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, String> {

    Authority findByAuthorityName(String authorityName);
}
