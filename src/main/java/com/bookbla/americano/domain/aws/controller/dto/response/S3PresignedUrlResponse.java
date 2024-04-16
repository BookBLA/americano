package com.bookbla.americano.domain.aws.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class S3PresignedUrlResponse {

    private String presignedUrl;
}
