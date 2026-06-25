package com.java.insurance.app.services.implementations;

import com.java.insurance.app.config.security.JwtService;
import com.java.insurance.app.services.AuthenticationService;
import com.java.insurance.app.services.UserService;
import com.java.insurance.app.models.User;
import com.java.insurance.app.payload.AuthenticationRequest;
import com.java.insurance.app.payload.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.java.insurance.app.constants.AppConstant.USER_REGISTERED;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Registers a new user in the system.
     *
     * @param user The user to register.
     * @return An authentication response containing the JWT token.
     */
    @Override
    public AuthenticationResponse register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.createUser(user);
        return AuthenticationResponse.builder().message(USER_REGISTERED).build();
    }

    /**
     * Logs in a user with the provided credentials.
     *
     * @param authenticationRequest The authentication request containing user credentials.
     * @return An authentication response containing the JWT token.
     */
    @Override
    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        User user = userService.getUserByEmail(authenticationRequest.getEmail());
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().message(jwtToken).build();
    }
}
