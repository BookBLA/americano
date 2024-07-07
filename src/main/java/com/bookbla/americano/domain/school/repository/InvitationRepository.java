package com.bookbla.americano.domain.school.repository;

import com.bookbla.americano.domain.school.repository.entity.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {

}
