package com.java.insurance.app.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorCode {
    public static final String VALIDATION_ERROR = "VALIDATION_ERROR";
    public static final String CONSTRAINT_VIOLATION_ERROR = "CONSTRAINT_VIOLATION_ERROR";
    public static final String AUTHENTICATION_ERROR = "AUTHENTICATION_ERROR";
    public static final String DATABASE_ERROR = "DATABASE_ERROR";
    public static final String NOTIFICATION_ERROR = "NOTIFICATION_ERROR";
    public static final String UNDERWRITER_NOT_FOUND = "UNDERWRITER_NOT_FOUND";
    public static final String ADMIN_NOT_FOUND = "ADMIN_NOT_FOUND";
    public static final String NO_POLICY_SPECIFIED = "NO_POLICY_SPECIFIED";
    public static final String APPLICATION_NOT_FOUND = "APPLICATION_NOT_FOUND";
    public static final String BENEFICIARY_NOT_FOUND = "BENEFICIARY_NOT_FOUND";
    public static final String USER_NOT_FOUND = "USER_NOT_FOUND";
    public static final String DISEASE_NOT_FOUND = "DISEASE_NOT_FOUND";
    public static final String POLICY_NOT_FOUND = "POLICY_NOT_FOUND";
    public static final String ROLE_NOT_FOUND = "ROLE_NOT_FOUND";
    public static final String APPLICATION_NOT_PENDING = "APPLICATION_NOT_PENDING";
}
