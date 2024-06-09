package com.bookbla.americano.domain.aws.service;

import com.bookbla.americano.domain.aws.enums.UploadType;

public interface S3Service {

    String movePhoto(UploadType from, UploadType to, String key);

    String getPreSignedUrl(UploadType type, String fileName);
}
