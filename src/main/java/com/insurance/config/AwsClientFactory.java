package com.insurance.config;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sqs.SqsClient;

public class AwsClientFactory {

    public static Region region() {
        return Region.of("eu-west-2");
    }

    public static S3Client s3() {
        return S3Client.builder().region(region()).build();
    }

    public static SnsClient sns() {
        return SnsClient.builder().region(region()).build();
    }

    public static SqsClient sqs() {
        return SqsClient.builder().region(region()).build();
    }
}