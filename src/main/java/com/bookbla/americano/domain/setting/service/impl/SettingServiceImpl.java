package com.bookbla.americano.domain.setting.service.impl;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.setting.controller.request.VersionCreateRequest;
import com.bookbla.americano.domain.setting.controller.response.VersionCreateResponse;
import com.bookbla.americano.domain.setting.controller.response.VersionReadResponse;
import com.bookbla.americano.domain.setting.repository.SettingRepository;
import com.bookbla.americano.domain.setting.repository.entity.Setting;
import com.bookbla.americano.domain.setting.service.SettingService;
import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SettingServiceImpl implements SettingService {

    private final SettingRepository settingRepository;

    @Override
    @Transactional(readOnly = true)
    public VersionReadResponse readVersion() {
        Setting setting = settingRepository.findLatest();
        return VersionReadResponse.from(setting);
    }

    @Override
    @Transactional
    public VersionCreateResponse createVersion(String version) {
        Setting setting = Setting.builder()
            .version(version)
            .build();

        settingRepository.save(setting);
        return VersionCreateResponse.from(setting);
    }
}
