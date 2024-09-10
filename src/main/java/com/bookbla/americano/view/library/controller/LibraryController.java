package com.bookbla.americano.view.library.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.view.library.controller.dto.MyLibraryReadResponse;
import com.bookbla.americano.view.library.controller.dto.OtherLibraryReadResponse;
import com.bookbla.americano.view.library.service.LibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "서재", description = "내 서재 & 상대 서재 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/library")
public class LibraryController {

    private final LibraryService libraryService;

    @Operation(summary = "내 서재 조회")
    @GetMapping
    public ResponseEntity<MyLibraryReadResponse> readMemberProfile(
            @Parameter(hidden = true) @User LoginUser loginUser
    ) {
        var response = libraryService.getLibraryProfile(loginUser.getMemberId());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "상대 서재 조회")
    @GetMapping("/target/{targetMemberId}")
    public ResponseEntity<OtherLibraryReadResponse> readMemberProfileByTarget(
            @Parameter(hidden = true) @User LoginUser loginUser,
            @PathVariable Long targetMemberId
    ) {
        var response = libraryService.getTargetLibraryProfile(loginUser.getMemberId(), targetMemberId);
        return ResponseEntity.ok(response);
    }
}
