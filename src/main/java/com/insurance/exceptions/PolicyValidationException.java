package com.insurance.exceptions;

public class PolicyValidationException extends RuntimeException{

    public PolicyValidationException(String message) {
        super(message);
    }
}
