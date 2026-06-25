package com.java.insurance.app.config.security;


import com.java.insurance.app.constants.SecurityConstant;
import com.java.insurance.app.models.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger jwtAuthlogger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Performs the actual filtering logic for authenticating requests.
     *
     * @param request     The HTTP servlet request.
     * @param response    The HTTP servlet response.
     * @param filterChain The filter chain to proceed with after authentication.
     * @throws ServletException If an exception occurs during the filtering process.
     * @throws IOException      If an I/O exception occurs.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader(SecurityConstant.AUTHORIZATION_HEADER);
        final String jwt;
        final String email;
        if (authHeader == null || !authHeader.startsWith(SecurityConstant.BEARER)) {
            jwtAuthlogger.info(SecurityConstant.AUTHENTICATION_HEADER_NULL);
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);
        email = jwtService.extractUsername(jwt);
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User userDetails = (User) this.userDetailsService.loadUserByUsername(email);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                jwtAuthlogger.info(SecurityConstant.TOKEN_VALIDATED);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
