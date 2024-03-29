package com.bookbla.americano.domain.policy.repository;

import com.bookbla.americano.domain.policy.Policy;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PolicyRepository extends JpaRepository<Policy, Long> {

    @Query("SELECT p FROM Policy p ORDER BY p.id")
    List<Policy> findAllSortedById();

}
