package com.insurance.model;

import java.time.LocalDate;

public class InsurancePolicy {
    private int id;
    private String regNumber;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private String policyType;
    private boolean isActive;


    public InsurancePolicy(String regNumber, LocalDate issueDate, LocalDate expiryDate, String policyType, boolean isActive) {
        this.regNumber = regNumber;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
        this.policyType = policyType;
        this.isActive = isActive;
    }


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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
