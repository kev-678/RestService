package com.org.service;


import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3StorageService {

    private static final String BUCKET_NAME =
            "employee-service-files-196253397204";

    private final S3Client s3Client;

    public S3StorageService(S3Client s3Client) {
        this.s3Client = s3Client;
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
}