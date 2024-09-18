package com.bookbla.americano.domain.notification.repository;

import com.bookbla.americano.domain.notification.repository.entity.InAppAlarmLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InAppAlarmLogRepository extends JpaRepository<InAppAlarmLog, Long> {
}
