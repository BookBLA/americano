package com.bookbla.americano.domain.member.repository;

import java.util.List;
import java.util.Optional;

import com.bookbla.americano.domain.member.repository.entity.MemberVerify;
import com.bookbla.americano.domain.member.enums.MemberVerifyStatus;
import com.bookbla.americano.domain.member.enums.MemberVerifyType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberVerifyRepository extends JpaRepository<MemberVerify, Long> {

    List<MemberVerify> findByMemberVerifyStatusAndMemberVerifyType(MemberVerifyStatus memberVerifyStatus, MemberVerifyType memberVerifyType);

}
