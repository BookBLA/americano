package com.bookbla.americano.domain.school.controller.docs;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.member.controller.dto.response.MemberInvitationResponse;
import com.bookbla.americano.domain.school.controller.dto.request.InvitationCodeEntryRequest;
import com.bookbla.americano.domain.school.controller.dto.response.InvitationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "초대", description = "초대 관련 API 모음")
public interface InvitationControllerDocs {

    @Operation(summary = "회원이 가진 초대코드를 조회합니다.")
    @GetMapping
    ResponseEntity<MemberInvitationResponse> readInvitationCode(
            @Parameter(hidden = true) @User LoginUser loginUser
    );

    @Operation(summary = "상대방으로부터 받은 초대코드를 입력합니다")
    @PostMapping("/invitation-code")
    ResponseEntity<InvitationResponse> entryInvitationCode(
            @Parameter(hidden = true) @User LoginUser loginUser,
            @RequestBody @Valid InvitationCodeEntryRequest request
    );

}
