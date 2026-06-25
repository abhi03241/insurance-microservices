package com.java.insurance.app.exception;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String errorCode;
    private Map<String, Object> errors;
    private HttpStatus httpStatus;
}
