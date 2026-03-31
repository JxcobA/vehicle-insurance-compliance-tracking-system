package com.insurance.model;

import java.time.LocalDate;

public class InsurancePolicy {

    private int policyId; // PK
    private String regNumber; // FK
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private String policyType;
    private boolean isActive;


    public InsurancePolicy(int policyId, String regNumber, LocalDate issueDate, LocalDate expiryDate, String policyType, boolean isActive) {
        this.policyId = policyId;
        this.regNumber = regNumber;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
        this.policyType = policyType;
        this.isActive = true;
    }


    public int getPolicyId() {
        return policyId;
    }
    public void setPolicyId(int policyId) {
        this.policyId = policyId;
    }

    public String getRegNumber() {
        return regNumber;
    }
    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }
    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getPolicyType() {
        return policyType;
    }
    public void setPolicyType(String policyType) {
        this.policyType = policyType;
    }

    public boolean isActive() {
        return isActive;
    }
    public void setActive(boolean active) {
        isActive = active;
    }
}
