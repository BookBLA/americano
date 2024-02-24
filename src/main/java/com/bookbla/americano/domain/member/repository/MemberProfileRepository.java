package com.bookbla.americano.domain.member.repository;

import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberProfileRepository extends JpaRepository<MemberProfile, Long> {

}