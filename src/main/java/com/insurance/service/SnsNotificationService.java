package com.insurance.service;

import com.insurance.config.AwsClientFactory;
import software.amazon.awssdk.services.sns.model.PublishRequest;

public class SnsNotificationService {

    private static final String TOPIC_ARN =
            "arn:aws:sns:eu-west-2:121736505725:vehicle-compliance-topic";

    public void sendSummary(int totalVehicles, int totalViolations, String s3Key) {

        String message = String.format(
                "Vehicle Insurance Compliance Report%n" +
                        "Total vehicles processed: %d%n" +
                        "Total violations found: %d%n" +
                        "Report uploaded to S3: %s",
                totalVehicles,
                totalViolations,
                s3Key
        );

        AwsClientFactory.sns().publish(
                PublishRequest.builder()
                        .topicArn(TOPIC_ARN)
                        .subject("Vehicle Compliance Job Summary")
                        .message(message)
                        .build()
        );
    }
}