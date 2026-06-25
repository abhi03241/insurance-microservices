package com.java.insurance.app.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccessConstants {
    public static final String HAS_ROLE_CUSTOMER = "hasRole('ROLE_CUSTOMER')";
    public static final String HAS_ROLE_ADMIN = "hasRole('ROLE_ADMIN')";
    public static final String HAS_ROLE_UNDERWRITER = "hasRole('ROLE_UNDERWRITER')";
    public static final String HAS_ROLES_USER_ADMIN = "hasAnyRole('ROLE_CUSTOMER', 'ROLE_ADMIN')";
}
