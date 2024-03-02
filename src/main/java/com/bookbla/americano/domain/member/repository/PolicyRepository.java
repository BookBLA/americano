package com.bookbla.americano.domain.member.repository;

import com.bookbla.americano.domain.member.repository.entity.Policy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PolicyRepository extends JpaRepository<Policy, Long> {

}
