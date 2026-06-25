package com.java.insurance.app.exception;


import com.java.insurance.app.constants.ErrorCode;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.ConnectException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

import static com.java.insurance.app.constants.AppConstant.COLON;
import static com.java.insurance.app.constants.AppConstant.COMMA;
import static com.java.insurance.app.constants.AppConstant.EXCEPTION;
import static com.java.insurance.app.constants.AppConstant.MESSAGE;

@ControllerAdvice
public class InsuranceExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(InsuranceExceptionHandler.class);

    @ExceptionHandler(InsuranceCustomException.class)
    public ResponseEntity<ErrorResponse> handleInsuranceException(InsuranceCustomException ie) {
        Map<String, Object> errors = new HashMap<>();
        errors.put(MESSAGE, ie.getMessage());
        logger.error(EXCEPTION + COLON + ie.getClass() + COMMA + MESSAGE + COLON + ie.getMessage());
        return new ResponseEntity<>(new ErrorResponse(ie.getErrorCode(), errors, HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException m) {
        Map<String, Object> errors = new HashMap<>();
        m.getBindingResult().getFieldErrors().forEach(fieldError -> {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            logger.error(EXCEPTION + COLON + m.getClass() + COMMA + MESSAGE + COLON + fieldError.getDefaultMessage());
        });
        return new ResponseEntity<>(new ErrorResponse(ErrorCode.VALIDATION_ERROR, errors, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ErrorResponse> handleMethodConstraintViolationException(ConstraintViolationException m) {
        Map<String, Object> errors = new HashMap<>();
        m.getConstraintViolations().forEach(fieldError -> {
            logger.error(EXCEPTION + COLON + m.getClass() + COMMA + MESSAGE + COLON + fieldError.getMessage());
            errors.put(fieldError.getPropertyPath().toString(), fieldError.getMessage());
        });
        return new ResponseEntity<>(new ErrorResponse(ErrorCode.CONSTRAINT_VIOLATION_ERROR, errors, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        Map<String, Object> errors = new HashMap<>();
        errors.put(EXCEPTION, ex.getClass());
        errors.put(MESSAGE, ex.getMessage());
        logger.error(EXCEPTION + COLON + ex.getClass() + COMMA + MESSAGE + COLON + ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse(ErrorCode.AUTHENTICATION_ERROR, errors, HttpStatus.FORBIDDEN), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({SQLIntegrityConstraintViolationException.class})
    public ResponseEntity<ErrorResponse> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex) {
        Map<String, Object> errors = new HashMap<>();
        errors.put(EXCEPTION, ex.getClass());
        errors.put(MESSAGE, ex.getMessage());
        logger.error(EXCEPTION + COLON + ex.getClass() + COMMA + MESSAGE + COLON + ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse(ErrorCode.DATABASE_ERROR, errors, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UsernameNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException ue) {
        Map<String, Object> errors = new HashMap<>();
        errors.put(EXCEPTION, ue.getClass());
        errors.put(MESSAGE, ue.getMessage());
        logger.error(EXCEPTION + COLON + ue.getClass() + COMMA + MESSAGE + COLON + ue.getMessage());
        return new ResponseEntity<>(new ErrorResponse(ErrorCode.DATABASE_ERROR, errors, HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException de) {
        Map<String, Object> errors = new HashMap<>();
        errors.put(EXCEPTION, de.getClass());
        errors.put(MESSAGE, de.getMessage());
        logger.error(EXCEPTION + COLON + de.getClass() + COMMA + MESSAGE + COLON + de.getMessage());
        return new ResponseEntity<>(new ErrorResponse(ErrorCode.DATABASE_ERROR, errors, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ConnectException.class})
    public ResponseEntity<ErrorResponse> handleRetryableException(ConnectException de) {
        Map<String, Object> errors = new HashMap<>();
        errors.put(EXCEPTION, de.getClass());
        errors.put(MESSAGE, de.getMessage());
        logger.error(EXCEPTION + COLON + de.getClass() + COMMA + MESSAGE + COLON + de.getMessage());
        return new ResponseEntity<>(new ErrorResponse(ErrorCode.NOTIFICATION_ERROR, errors, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }
}
