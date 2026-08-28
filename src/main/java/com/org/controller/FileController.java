package com.org.controller;
import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.org.service.S3StorageService;

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
}