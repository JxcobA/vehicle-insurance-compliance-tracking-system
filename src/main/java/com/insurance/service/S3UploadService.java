package com.insurance.service;

import com.insurance.config.AwsClientFactory;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.file.Path;

public class S3UploadService {

    private static final String BUCKET_NAME = "vehicle-compliance-bucket";

    public String upload(String filePath) {

        // Use the CSV filename itself
        String fileName = Path.of(filePath).getFileName().toString();
        String s3Key = "reports/" + fileName;

        AwsClientFactory.s3().putObject(
                PutObjectRequest.builder()
                        .bucket(BUCKET_NAME)
                        .key(s3Key)
                        .build(),
                RequestBody.fromFile(Path.of(filePath))
        );

        return s3Key;
    }
}