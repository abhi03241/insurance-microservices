package com.java.insurance.app.config.security;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Data
@RequiredArgsConstructor
public class ApplicationConfiguration {
    private final UserDetailsService userDetailsService;

    /**
     * Retrieves the password encoder bean used for encoding and decoding passwords.
     *
     * @return The password encoder bean.
     */
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Retrieves the authentication provider bean used for authenticating users.
     *
     * @return The authentication provider bean.
     */
    @Bean
    public AuthenticationProvider getAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(getPasswordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }

    /**
     * Retrieves the authentication manager bean used for managing authentication within the application.
     *
     * @param configuration The authentication configuration.
     * @return The authentication manager bean.
     * @throws Exception If an exception occurs while retrieving the authentication manager.
     */
    @Bean
    public AuthenticationManager getAuthenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
