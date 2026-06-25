package com.java.insurance.app.config.security;

import com.java.insurance.app.constants.SecurityConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Data
@Component(SecurityConstant.APPLICATION_AUTHENTICATION_ENTRY_POINT_NAME)
public class ApplicationAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Qualifier(SecurityConstant.HANDLER_EXCEPTION_RESOLVER)
    @Autowired
    private HandlerExceptionResolver resolver;

    /**
     * Called when an unauthenticated user attempts to access a protected resource.
     * This method handles the beginning of the authentication process, typically by sending an error response
     * or redirecting the user to the login page.
     *
     * @param request       The HTTP servlet request.
     * @param response      The HTTP servlet response.
     * @param authException The authentication exception that occurred.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        resolver.resolveException(request, response, null, authException);
    }
}
