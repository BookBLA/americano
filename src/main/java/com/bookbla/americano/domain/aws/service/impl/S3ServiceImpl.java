package com.bookbla.americano.domain.aws.service.impl;

import java.net.URL;
import java.util.Date;

import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.aws.enums.UploadType;
import com.bookbla.americano.domain.aws.exception.AwsException;
import com.bookbla.americano.domain.aws.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

    private static final String IMAGE_POSTFIX = ".jpg";

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.cloud-front-url}")
    private String url;

    @Override
    public String movePhoto(UploadType source, UploadType destination, Long key) {
        String fileName = key.toString() + IMAGE_POSTFIX;

        String sourceKey = createPath(source.getType(), fileName);
        String destinationKey = createPath(destination.getType(), fileName);

        try {
            CopyObjectRequest copyObjectRequest = new CopyObjectRequest(bucket, sourceKey, bucket, destinationKey);
            amazonS3.copyObject(copyObjectRequest);

            DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, sourceKey);
            amazonS3.deleteObject(deleteObjectRequest);
        } catch (SdkClientException e) {
            throw new BaseException(AwsException.AWS_COMMUNICATION_ERROR, e);
        }

        return url + destination.getType() + "/" + fileName;
    }

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
