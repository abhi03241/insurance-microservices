package com.java.insurance.app.config.security;

import com.java.insurance.app.constants.SecurityConstant;
import com.java.insurance.app.exception.InsuranceExceptionHandler;
import lombok.Data;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebSecurity
@Data
@EnableWebMvc
public class SecurityConfiguration {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthFilter;
    @Qualifier(SecurityConstant.APPLICATION_AUTHENTICATION_ENTRY_POINT_NAME)
    private final AuthenticationEntryPoint authEntryPoint;
    private final InsuranceExceptionHandler insuranceExceptionHandler;

    /**
     * Configures the security filter chain for handling HTTP security.
     *
     * @param httpSecurity The HttpSecurity object to configure.
     * @return The SecurityFilterChain instance.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry.requestMatchers("/v3/api-docs/**",
                "/swagger-ui/**", "/swagger-ui.html","/api/v1/auth/**", "/api/v1/role/**", "/api/v1/diseases/**", "/api/v1/admin/**").permitAll().anyRequest().authenticated()).sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authenticationProvider(authenticationProvider).addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class).exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(authEntryPoint));
        return httpSecurity.build();

    }

}