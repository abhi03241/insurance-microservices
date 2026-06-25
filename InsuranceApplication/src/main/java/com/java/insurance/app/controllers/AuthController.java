package com.java.insurance.app.controllers;

import com.java.insurance.app.services.AuthenticationService;
import com.java.insurance.app.models.User;
import com.java.insurance.app.payload.AuthenticationRequest;
import com.java.insurance.app.payload.AuthenticationResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.java.insurance.app.constants.AuthConstants.LOGIN;
import static com.java.insurance.app.constants.AuthConstants.REGISTER;

@RestController
@RequiredArgsConstructor
@Tag(name = "Authentication APIs")
public class AuthController {
    private final AuthenticationService authenticationService;

    /**
     * Registers a new user.
     *
     * @param user The user object containing registration details.
     * @return ResponseEntity containing the authentication response.
     */
    @PostMapping(REGISTER)
    public ResponseEntity<AuthenticationResponse> register(@RequestBody User user) {
        return ResponseEntity.ok(authenticationService.register(user));
    }

    /**
     * Logs in a user.
     *
     * @param authenticationRequest The authentication request containing user credentials.
     * @return ResponseEntity containing the authentication response.
     */
    @PostMapping(LOGIN)
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(authenticationService.login(authenticationRequest));
    }
}
