package com.bookbla.americano.domain.setting.service;

import com.bookbla.americano.domain.setting.controller.request.VersionCreateRequest;
import com.bookbla.americano.domain.setting.controller.response.VersionCreateResponse;
import com.bookbla.americano.domain.setting.controller.response.VersionReadResponse;

public interface SettingService {

    VersionReadResponse readVersion();

    VersionCreateResponse createVersion(String version);
}
