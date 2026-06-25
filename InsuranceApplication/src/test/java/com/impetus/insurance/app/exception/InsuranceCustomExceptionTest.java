package com.java.insurance.app.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class InsuranceCustomExceptionTest {
    /**
     * Method under test:
     * {@link InsuranceCustomException#InsuranceCustomException(String, String)}
     */
    @Test
    void testNewInsuranceCustomException() {
        // Arrange and Act
        InsuranceCustomException actualInsuranceCustomException = new InsuranceCustomException("An error occurred",
                "An error occurred");

        // Assert
        assertEquals("An error occurred", actualInsuranceCustomException.getErrorCode());
        assertEquals("An error occurred", actualInsuranceCustomException.getLocalizedMessage());
        assertEquals("An error occurred", actualInsuranceCustomException.getMessage());
        assertNull(actualInsuranceCustomException.getCause());
        assertEquals(0, actualInsuranceCustomException.getSuppressed().length);
    }
}