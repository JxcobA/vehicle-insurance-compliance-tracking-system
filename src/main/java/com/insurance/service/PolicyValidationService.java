package com.insurance.service;

import com.insurance.model.InsurancePolicy;

public class PolicyValidationService {

    public void validatePolicy(InsurancePolicy policy) {

        if (policy == null) {
            throw new IllegalArgumentException("Policy cannot be null");
        }

        if (policy.getRegNumber() == null || policy.getRegNumber().isBlank()) {
            throw new IllegalArgumentException("Registration number cannot be null or empty");
        }

        if (policy.getPolicyType() == null || policy.getPolicyType().isBlank()) {
            throw new IllegalArgumentException("Policy type cannot be null or empty");
        }

        if (policy.getIssueDate() == null || policy.getExpiryDate() == null) {
            throw new IllegalArgumentException("Issue and expiry dates are required");
        }

        if (!policy.getExpiryDate().isAfter(policy.getIssueDate())) {
            throw new IllegalArgumentException("Expiry date must be after issue date");
        }

    }
}
