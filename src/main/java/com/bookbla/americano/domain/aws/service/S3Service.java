package com.bookbla.americano.domain.aws.service;

import com.bookbla.americano.domain.aws.controller.enums.UploadType;

public interface S3Service {
    String getPreSignedUrl(UploadType type, String fileName);
}
