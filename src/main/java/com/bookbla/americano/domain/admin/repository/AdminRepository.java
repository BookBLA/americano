package com.bookbla.americano.domain.admin.repository;


import java.util.Optional;

import com.bookbla.americano.domain.admin.repository.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByUserId(String userId);

}
