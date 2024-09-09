package com.bookbla.americano.domain.aws.service;

import java.net.URL;
import java.util.Date;

import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.aws.enums.UploadType;
import com.bookbla.americano.domain.aws.exception.AwsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String getPreSignedUrl(UploadType type, String fileName) {
        String objectKey = createPath(type.getType(), fileName);
        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePreSignedUrlRequest(bucket, objectKey);

        return requestPresignedUrl(generatePresignedUrlRequest);
    }

    private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(String bucket, String objectKey) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucket, objectKey)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(getPreSignedUrlExpiration());

        generatePresignedUrlRequest.addRequestParameter(
                Headers.S3_CANNED_ACL,
                CannedAccessControlList.PublicRead.toString());

        return generatePresignedUrlRequest;
    }

    private String requestPresignedUrl(GeneratePresignedUrlRequest request) {
        try {
            URL url = amazonS3.generatePresignedUrl(request);
            return url.toString();
        } catch (SdkClientException e) {
            throw new BaseException(AwsException.AWS_COMMUNICATION_ERROR);
        }
    }

    private Date getPreSignedUrlExpiration() {
        return new Date(new Date().getTime() + 1000 * 60 * 2);
    }

    private String createPath(String prefix, String fileName) {
        return String.format("%s/%s", prefix, fileName);
    }
}
