package com.java.insurance.app.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashSet;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {InsuranceExceptionHandler.class})
@ExtendWith(SpringExtension.class)
class InsuranceExceptionHandlerTest {
    @Autowired
    private InsuranceExceptionHandler insuranceExceptionHandler;

    /**
     * Method under test:
     * {@link InsuranceExceptionHandler#handleInsuranceException(InsuranceCustomException)}
     */
    @Test
    void testHandleInsuranceException() {
        // Arrange and Act
        ResponseEntity<ErrorResponse> actualHandleInsuranceExceptionResult = insuranceExceptionHandler.handleInsuranceException(new InsuranceCustomException("An error occurred", "An error occurred"));

        // Assert
        HttpStatusCode expectedHttpStatus = actualHandleInsuranceExceptionResult.getStatusCode();
        assertSame(expectedHttpStatus, actualHandleInsuranceExceptionResult.getBody().getHttpStatus());
    }

    /**
     * Method under test:
     * {@link InsuranceExceptionHandler#handleInsuranceException(InsuranceCustomException)}
     */
    @Test
    void testHandleInsuranceException2() {
        // Arrange
        InsuranceCustomException ie = mock(InsuranceCustomException.class);
        when(ie.getErrorCode()).thenReturn("An error occurred");
        when(ie.getMessage()).thenReturn("Not all who wander are lost");

        // Act
        ResponseEntity<ErrorResponse> actualHandleInsuranceExceptionResult = insuranceExceptionHandler.handleInsuranceException(ie);

        // Assert
        verify(ie).getErrorCode();
        verify(ie, atLeast(1)).getMessage();
        HttpStatusCode expectedHttpStatus = actualHandleInsuranceExceptionResult.getStatusCode();
        assertSame(expectedHttpStatus, actualHandleInsuranceExceptionResult.getBody().getHttpStatus());
    }

    /**
     * Method under test:
     * {@link InsuranceExceptionHandler#handleMethodArgumentNotValidException(MethodArgumentNotValidException)}
     */
    @Test
    void testHandleMethodArgumentNotValidException() {
        // Arrange and Act
        ResponseEntity<ErrorResponse> actualHandleMethodArgumentNotValidExceptionResult = insuranceExceptionHandler.handleMethodArgumentNotValidException(new MethodArgumentNotValidException(null, new BindException("Target", "Object Name")));

        // Assert
        HttpStatusCode expectedHttpStatus = actualHandleMethodArgumentNotValidExceptionResult.getStatusCode();
        assertSame(expectedHttpStatus, actualHandleMethodArgumentNotValidExceptionResult.getBody().getHttpStatus());
    }

    /**
     * Method under test:
     * {@link InsuranceExceptionHandler#handleMethodArgumentNotValidException(MethodArgumentNotValidException)}
     */
    @Test
    void testHandleMethodArgumentNotValidException2() {
        // Arrange
        MethodParameter parameter = mock(MethodParameter.class);

        // Act
        ResponseEntity<ErrorResponse> actualHandleMethodArgumentNotValidExceptionResult = insuranceExceptionHandler.handleMethodArgumentNotValidException(new MethodArgumentNotValidException(parameter, new BindException("Target", "Object Name")));

        // Assert
        HttpStatusCode expectedHttpStatus = actualHandleMethodArgumentNotValidExceptionResult.getStatusCode();
        assertSame(expectedHttpStatus, actualHandleMethodArgumentNotValidExceptionResult.getBody().getHttpStatus());
    }

    /**
     * Method under test:
     * {@link InsuranceExceptionHandler#handleMethodConstraintViolationException(ConstraintViolationException)}
     */
    @Test
    void testHandleMethodConstraintViolationException() {
        // Arrange and Act
        ResponseEntity<ErrorResponse> actualHandleMethodConstraintViolationExceptionResult = insuranceExceptionHandler.handleMethodConstraintViolationException(new ConstraintViolationException(new HashSet<>()));

        // Assert
        HttpStatusCode expectedHttpStatus = actualHandleMethodConstraintViolationExceptionResult.getStatusCode();
        assertSame(expectedHttpStatus, Objects.requireNonNull(actualHandleMethodConstraintViolationExceptionResult.getBody()).getHttpStatus());
    }

    /**
     * Method under test:
     * {@link InsuranceExceptionHandler#handleMethodConstraintViolationException(ConstraintViolationException)}
     */
    @Test
    void testHandleMethodConstraintViolationException2() {
        // Arrange
        ConstraintViolation constraintViolation = mock(ConstraintViolation.class);
        when(constraintViolation.getPropertyPath()).thenReturn(PathImpl.createRootPath());
        when(constraintViolation.getMessage()).thenReturn("Not all who wander are lost");

        HashSet<ConstraintViolation<?>> constraintViolations = new HashSet<>();
        constraintViolations.add(constraintViolation);

        // Act
        ResponseEntity<ErrorResponse> actualHandleMethodConstraintViolationExceptionResult = insuranceExceptionHandler.handleMethodConstraintViolationException(new ConstraintViolationException(constraintViolations));

        // Assert
        verify(constraintViolation, atLeast(1)).getMessage();
        verify(constraintViolation, atLeast(1)).getPropertyPath();
        HttpStatusCode expectedHttpStatus = actualHandleMethodConstraintViolationExceptionResult.getStatusCode();
        assertSame(expectedHttpStatus, actualHandleMethodConstraintViolationExceptionResult.getBody().getHttpStatus());
    }

    /**
     * Method under test:
     * {@link InsuranceExceptionHandler#handleMethodConstraintViolationException(ConstraintViolationException)}
     */
    @Test
    void testHandleMethodConstraintViolationException3() {
        // Arrange
        ConstraintViolation<Object> constraintViolation = mock(ConstraintViolation.class);
        when(constraintViolation.getPropertyPath()).thenReturn(PathImpl.createRootPath());
        when(constraintViolation.getMessage()).thenReturn("Not all who wander are lost");
        ConstraintViolation<Object> constraintViolation2 = mock(ConstraintViolation.class);
        when(constraintViolation2.getPropertyPath()).thenReturn(PathImpl.createRootPath());
        when(constraintViolation2.getMessage()).thenReturn("Not all who wander are lost");

        HashSet<ConstraintViolation<?>> constraintViolations = new HashSet<>();
        constraintViolations.add(constraintViolation2);
        constraintViolations.add(constraintViolation);

        // Act
        ResponseEntity<ErrorResponse> actualHandleMethodConstraintViolationExceptionResult = insuranceExceptionHandler.handleMethodConstraintViolationException(new ConstraintViolationException(constraintViolations));

        // Assert
        verify(constraintViolation2, atLeast(1)).getMessage();
        verify(constraintViolation, atLeast(1)).getMessage();
        verify(constraintViolation2, atLeast(1)).getPropertyPath();
        verify(constraintViolation, atLeast(1)).getPropertyPath();
        HttpStatusCode expectedHttpStatus = actualHandleMethodConstraintViolationExceptionResult.getStatusCode();
        assertSame(expectedHttpStatus, actualHandleMethodConstraintViolationExceptionResult.getBody().getHttpStatus());
    }


}