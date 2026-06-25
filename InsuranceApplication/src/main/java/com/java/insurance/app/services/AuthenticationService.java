package com.java.insurance.app.services;

import com.java.insurance.app.models.User;
import com.java.insurance.app.payload.AuthenticationRequest;
import com.java.insurance.app.payload.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse register(User user);

    AuthenticationResponse login(AuthenticationRequest authenticationRequest);

}
