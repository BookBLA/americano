package com.bookbla.americano.domain.school.repository;

import java.util.Optional;

import com.bookbla.americano.domain.school.repository.entity.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    Optional<Invitation> findByInvitedMemberId(Long invitedMemberId);

}
