package com.bookbla.americano.domain.user.repository;

import com.bookbla.americano.domain.user.repository.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = "userAuthoritySet")
    Optional<User> findOneWithUserAuthorityByEmail(String email);

    Optional<User> findByEmail(String email);

}
