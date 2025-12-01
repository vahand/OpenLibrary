package org.example.library.services;

import org.example.library.utils.auth.LoginRequest;
import org.example.library.utils.auth.LoginResponse;
import org.example.library.utils.auth.RegisterRequest;
import org.example.library.utils.auth.RegisterResponse;

public interface KeycloakAuthService {
    LoginResponse login(LoginRequest loginRequest);
    RegisterResponse register(RegisterRequest registerRequest);
}
