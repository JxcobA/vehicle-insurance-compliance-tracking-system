package com.insurance.service;

import com.insurance.config.AwsClientFactory;
import com.insurance.model.ViolationRecord;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

public class SqsPublisherService {

    private static final String QUEUE_URL =
            "https://sqs.eu-west-2.amazonaws.com/121736505725/vehicle-compliance-queue";

    public void sendMessage(ViolationRecord violation) {

        String message = String.format(
                "Violation detected%n" +
                        "Registration: %s%n" +
                        "Insurance expiry: %s%n" +
                        "Last trip start: %s%n" +
                        "Last trip end: %s%n" +
                        "Reason: %s",
                violation.getRegNumber(),
                violation.getExpiryDate(),
                violation.getLastTripStart(),
                violation.getLastTripEnd(),
                violation.getReason()
        );

        AwsClientFactory.sqs().sendMessage(
                SendMessageRequest.builder()
                        .queueUrl(QUEUE_URL)
                        .messageBody(message)
                        .build()
        );
    }
}