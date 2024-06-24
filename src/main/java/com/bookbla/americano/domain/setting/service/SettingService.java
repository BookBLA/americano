package com.bookbla.americano.domain.setting.service;

import com.bookbla.americano.domain.setting.controller.response.VersionCreateResponse;
import com.bookbla.americano.domain.setting.controller.response.VersionReadResponse;
import com.bookbla.americano.domain.setting.repository.SettingRepository;
import com.bookbla.americano.domain.setting.repository.entity.Setting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SettingService {

    private final SettingRepository settingRepository;

    @Transactional(readOnly = true)
    public VersionReadResponse readVersion() {
        Setting setting = settingRepository.findLatest();
        return VersionReadResponse.from(setting);
    }

    @Transactional
    public VersionCreateResponse createVersion(String version) {
        Setting setting = Setting.builder()
                .version(version)
                .build();

        settingRepository.save(setting);
        return VersionCreateResponse.from(setting);
    }
}
