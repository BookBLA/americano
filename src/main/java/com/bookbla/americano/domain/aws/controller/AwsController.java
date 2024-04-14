package com.bookbla.americano.domain.aws.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.aws.controller.dto.response.S3PresignedUrlResponse;
import com.bookbla.americano.domain.aws.enums.UploadType;
import com.bookbla.americano.domain.aws.service.S3Service;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/aws")
public class AwsController {

    private final S3Service s3Service;

    @GetMapping("/s3/presigned-url/{uploadType}")
    public ResponseEntity<S3PresignedUrlResponse> getS3PresignedUrl(
            @Parameter(hidden = true) @User LoginUser loginUser,
            @PathVariable String uploadType,
            @RequestParam String fileName
    ) {
        S3PresignedUrlResponse s3PresignedUrlResponse = S3PresignedUrlResponse.builder()
                .presignedUrl(s3Service.getPreSignedUrl(UploadType.from(uploadType), fileName))
                .build();

        return ResponseEntity.ok(s3PresignedUrlResponse);
    }
}

