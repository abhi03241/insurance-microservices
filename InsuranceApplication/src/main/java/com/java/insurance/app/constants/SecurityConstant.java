package com.java.insurance.app.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityConstant {
    public static final String APPLICATION_AUTHENTICATION_ENTRY_POINT_NAME = "applicationAuthenticationEntryPoint";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER = "Bearer";
    public static final String AUTHENTICATION_HEADER_NULL = "Authentication header null or invalid";
    public static final String TOKEN_VALIDATED = "Token validated successfully";
    public static final String HANDLER_EXCEPTION_RESOLVER = "handlerExceptionResolver";
}
