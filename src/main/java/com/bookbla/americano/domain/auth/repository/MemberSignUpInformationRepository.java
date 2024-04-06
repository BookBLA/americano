package com.bookbla.americano.domain.auth.repository;

import java.util.Optional;

import com.bookbla.americano.domain.auth.repository.entity.MemberSignUpInformation;
import com.bookbla.americano.domain.member.enums.MemberType;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberSignUpInformationRepository extends JpaRepository<MemberSignUpInformation, Long> {

    Optional<MemberSignUpInformation> findByMemberTypeAndEmail(MemberType memberType, String email);

}


