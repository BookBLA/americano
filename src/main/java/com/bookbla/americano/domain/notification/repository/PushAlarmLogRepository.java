package com.bookbla.americano.domain.notification.repository;

import com.bookbla.americano.domain.notification.repository.entity.PushAlarmLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PushAlarmLogRepository extends JpaRepository<PushAlarmLog, Long> {


}
