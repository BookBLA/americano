package com.bookbla.americano.domain.setting.repository;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.setting.exception.SettingExceptionType;
import com.bookbla.americano.domain.setting.repository.entity.Setting;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingRepository extends JpaRepository<Setting, Long> {

    default Setting findLatest() {
        PageRequest pageRequest = PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "id"));
        return findAll(pageRequest).stream().findFirst()
            .orElseThrow(() -> new BaseException(SettingExceptionType.SETTING_NOT_FOUND));
    }
}
