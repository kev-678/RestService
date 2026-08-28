package com.org.service;


import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import java.time.Duration;

import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Service
public class S3StorageService {

    private static final String BUCKET_NAME =
            "employee-service-files-196253397204";
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    public S3StorageService(
            S3Client s3Client,
            S3Presigner s3Presigner) {

        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
    }


    public String upload(MultipartFile file) throws IOException {

        String key = "employees/" + System.currentTimeMillis()
                + "-" + file.getOriginalFilename();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(key)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(
                request,
                RequestBody.fromBytes(file.getBytes())
        );

        return key;
    }




    public ResponseBytes<GetObjectResponse> download(String key) {

        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(key)
                .build();

        return s3Client.getObjectAsBytes(request);
    }


    public String generateUploadUrl(
            String key,
            String contentType) {

        PutObjectRequest putObjectRequest =
                PutObjectRequest.builder()
                        .bucket(BUCKET_NAME)
                        .key(key)
                        .contentType(contentType)
                        .build();

        PutObjectPresignRequest presignRequest =
                PutObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMinutes(10))
                        .putObjectRequest(putObjectRequest)
                        .build();

        return s3Presigner
                .presignPutObject(presignRequest)
                .url()
                .toString();
    }


    public String generateDownloadUrl(String key) {

        GetObjectRequest getObjectRequest =
                GetObjectRequest.builder()
                        .bucket(BUCKET_NAME)
                        .key(key)
                        .build();

        GetObjectPresignRequest presignRequest =
                GetObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMinutes(10))
                        .getObjectRequest(getObjectRequest)
                        .build();

        return s3Presigner
                .presignGetObject(presignRequest)
                .url()
                .toString();
    }
}