package com.bookbla.americano.domain.member.repository;


import com.bookbla.americano.domain.member.repository.entity.MemberStatusLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberStatusLogRepository extends JpaRepository<MemberStatusLog, Long> {

}
