package com.penguinshop.service;

import com.penguinshop.domain.USER_ROLE;
import com.penguinshop.model.LoginRequest;
import com.penguinshop.response.AuthResponse;
import com.penguinshop.response.SignupRequest;

public interface AuthService {
    void sendOtp(String email, USER_ROLE role) throws Exception;
    String createUser(SignupRequest req) throws Exception; 
    AuthResponse signIn(LoginRequest req);
}
