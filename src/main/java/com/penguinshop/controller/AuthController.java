package com.penguinshop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.penguinshop.domain.USER_ROLE;
import com.penguinshop.repository.UserRepository;
import com.penguinshop.request.LoginOtpRequest;
import com.penguinshop.response.ApiResponse;
import com.penguinshop.response.AuthResponse;
import com.penguinshop.response.SignupRequest;
import com.penguinshop.service.AuthService;
import com.penguinshop.model.LoginRequest;

import lombok.RequiredArgsConstructor;

/**
 * REST controller responsible for handling user sign up requests
 */

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    // As AuthServiceImpl is annotated with @Service and implements AuthService,
    // Spring injects an instance of AuthServiceImpl into authService
    // So AuthServiceImpl is actually automatically injected into authService
    // (really cool!!!!)
    private final AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginRequest req) throws Exception {
        AuthResponse res = authService.signIn(req);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody SignupRequest req) throws Exception {
        String jwt = authService.createUser(req);
        AuthResponse res = new AuthResponse();
        res.setJwt(jwt);
        res.setMessage("Registering succeeded");
        res.setRole(USER_ROLE.ROLE_CUSTOMER);

        return ResponseEntity.ok(res);
    }

    @PostMapping("/send/login-signup-otp")
    public ResponseEntity<ApiResponse> sentOtpHandler(@RequestBody LoginOtpRequest req) throws Exception {
        if (req.getRole() == null) {
            System.out.println("From AController: Role is empty! Role - " + req.getRole());
        }
        authService.sendOtp(req.getEmail(), req.getRole());

        ApiResponse res = new ApiResponse();
        res.setMessage("OTP sent successfully!");

        return ResponseEntity.ok(res);
    }

}
