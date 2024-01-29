package com.bookbla.americano.domain.user.repository;

import com.bookbla.americano.domain.user.repository.entity.UserAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuthorityRepository extends JpaRepository<UserAuthority, Long> {
}
