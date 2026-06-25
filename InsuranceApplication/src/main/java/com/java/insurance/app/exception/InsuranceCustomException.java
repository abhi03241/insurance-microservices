package com.java.insurance.app.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class InsuranceCustomException extends RuntimeException {
    private final String errorCode;

    public InsuranceCustomException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
