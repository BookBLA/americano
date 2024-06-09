package com.bookbla.americano.domain.aws.controller;

import com.bookbla.americano.domain.aws.controller.dto.response.S3PresignedUrlResponse;
import com.bookbla.americano.domain.aws.enums.UploadType;
import com.bookbla.americano.domain.aws.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/aws")
public class AwsController {

    private final S3Service s3Service;

    @GetMapping("/s3/presigned-url/{uploadType}")
    public ResponseEntity<S3PresignedUrlResponse> getS3PresignedUrl(
            @PathVariable String uploadType,
            @RequestParam String fileName
    ) {
        S3PresignedUrlResponse s3PresignedUrlResponse = S3PresignedUrlResponse.builder()
                .presignedUrl(s3Service.getPreSignedUrl(UploadType.from(uploadType), fileName))
                .build();

        return ResponseEntity.ok(s3PresignedUrlResponse);
    }
}

