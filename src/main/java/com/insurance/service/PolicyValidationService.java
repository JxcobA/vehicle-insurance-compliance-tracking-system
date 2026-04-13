package com.insurance.service;

import com.insurance.exceptions.PolicyValidationException;
import com.insurance.model.InsurancePolicy;

public class PolicyValidationService implements Validatable<InsurancePolicy> {

    @Override
    public void validate(InsurancePolicy policy) {
        validatePolicy(policy);
    }

    public void validatePolicy(InsurancePolicy policy) {

        if (policy == null) {
            throw new PolicyValidationException("Policy cannot be null");
        }

        if (policy.getRegNumber() == null || policy.getRegNumber().isBlank()) {
            throw new PolicyValidationException("Registration number cannot be null or empty");
        }

        if (policy.getPolicyType() == null || policy.getPolicyType().isBlank()) {
            throw new PolicyValidationException("Policy type cannot be null or empty");
        }

        if (policy.getIssueDate() == null || policy.getExpiryDate() == null) {
            throw new PolicyValidationException("Issue and expiry dates are required");
        }

        if (!policy.getExpiryDate().isAfter(policy.getIssueDate())) {
            throw new PolicyValidationException("Expiry date must be after issue date");
        }
    }
}