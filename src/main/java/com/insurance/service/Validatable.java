package com.insurance.service;

public interface Validatable<T> {

    void validate(T input);
}
