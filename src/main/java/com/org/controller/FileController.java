package com.org.controller;
import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.org.service.S3StorageService;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@RestController
@RequestMapping("/v1/files")
public class FileController {

    private final S3StorageService s3StorageService;

    public FileController(S3StorageService s3StorageService) {
        this.s3StorageService = s3StorageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(
            @RequestParam("file") MultipartFile file) throws IOException {

        String key = s3StorageService.upload(file);

        return ResponseEntity.ok(key);
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> download(
            @RequestParam("key") String key) {

        ResponseBytes<GetObjectResponse> object =
                s3StorageService.download(key);

        String contentType =
                object.response().contentType() != null
                        ? object.response().contentType()
                        : "application/octet-stream";

        return ResponseEntity.ok()
                .header(
                        "Content-Disposition",
                        "attachment; filename=\"" +
                                key.substring(key.lastIndexOf("/") + 1) +
                                "\""
                )
                .contentType(MediaType.parseMediaType(contentType))
                .body(object.asByteArray());
    }


    @GetMapping("/upload-url")
    public ResponseEntity<String> getUploadUrl(
            @RequestParam("fileName") String fileName,
            @RequestParam("contentType") String contentType) {

        String key =
                "employees/" +
                        System.currentTimeMillis() +
                        "-" +
                        fileName;

        return ResponseEntity.ok(
                s3StorageService.generateUploadUrl(
                        key,
                        contentType
                )
        );
    }


    @GetMapping("/download-url")
    public ResponseEntity<String> getDownloadUrl(
            @RequestParam("key") String key) {

        return ResponseEntity.ok(
                s3StorageService.generateDownloadUrl(key)
        );
    }
}