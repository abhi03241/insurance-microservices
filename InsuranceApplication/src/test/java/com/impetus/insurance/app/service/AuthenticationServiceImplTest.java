package com.java.insurance.app.service;

import com.java.insurance.app.config.security.JwtService;
import com.java.insurance.app.models.User;
import com.java.insurance.app.payload.AuthenticationRequest;
import com.java.insurance.app.payload.AuthenticationResponse;
import com.java.insurance.app.services.UserService;
import com.java.insurance.app.services.implementations.AuthenticationServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import static org.assertj.core.api.AssertionsForClassTypes.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;

@SpringBootTest
public class AuthenticationServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticationServiceImpl authService;
    @Mock
    private AuthenticationManager authenticationManager;

    @Test
    @WithMockUser
    public void register_Test() {
        // Given
        User testUser = new User();
        testUser.setName("testUser");
        testUser.setPassword("password");

        // Mocking behavior of passwordEncoder
        when(passwordEncoder.encode(testUser.getPassword())).thenReturn("encodedPassword");

        // Mocking behavior of userService
        when(userService.createUser(testUser)).thenReturn(testUser);

        // Mocking behavior of jwtService
        when(jwtService.generateToken(testUser)).thenReturn("jwtToken");

        // When
        AuthenticationResponse response = authService.register(testUser);

        // Then
        // Verify that password was encoded
        verify(passwordEncoder).encode("password");

        // Asserting the response
        assertEquals("User registered", response.getMessage());
    }

    @Test
    public void testLogin() {
        // Given
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("test@example.com");
        authenticationRequest.setPassword("password");

        // Mocking behavior of userService
        User testUser = new User();
        testUser.setEmail(authenticationRequest.getEmail());
        when(userService.getUserByEmail(authenticationRequest.getEmail())).thenReturn(testUser);

        // Mocking behavior of jwtService
        when(jwtService.generateToken(testUser)).thenReturn("jwtToken");

        // Mocking behavior of authenticationManager
        Authentication authentication = mock(Authentication.class);
        try {
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authentication);
        } catch (AuthenticationException e) {
            fail("AuthenticationException occurred during authentication: " + e.getMessage());
        }

        // When
        AuthenticationResponse response = authService.login(authenticationRequest);

        // Then
        // Verify that authenticationManager.authenticate was called
        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
        );

        // Verify that userService.getUserByEmail was called
        verify(userService).getUserByEmail(authenticationRequest.getEmail());

    }
}
