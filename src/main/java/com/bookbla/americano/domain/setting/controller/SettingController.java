package com.bookbla.americano.domain.setting.controller;

import com.bookbla.americano.domain.setting.controller.request.VersionCreateRequest;
import com.bookbla.americano.domain.setting.controller.response.VersionCreateResponse;
import com.bookbla.americano.domain.setting.controller.response.VersionReadResponse;
import com.bookbla.americano.domain.setting.service.SettingService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/settings")
@RequiredArgsConstructor
public class SettingController {

    private final SettingService settingService;

    @GetMapping("/versions")
    public ResponseEntity<VersionReadResponse> readVersion() {
        VersionReadResponse versionReadResponse = settingService.readVersion();
        return ResponseEntity.ok(versionReadResponse);
    }

    @PostMapping("/versions")
    public ResponseEntity<VersionCreateResponse> createVersion(
        @RequestBody @Valid VersionCreateRequest versionCreateRequest) {
        VersionCreateResponse versionCreateResponse = settingService.createVersion(
            versionCreateRequest.getVersion());
        return ResponseEntity.ok(versionCreateResponse);
    }
}
