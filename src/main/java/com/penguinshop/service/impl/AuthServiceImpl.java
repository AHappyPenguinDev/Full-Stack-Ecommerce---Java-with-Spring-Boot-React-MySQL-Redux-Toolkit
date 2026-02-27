package com.penguinshop.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.penguinshop.config.JwtProvider;
import com.penguinshop.domain.USER_ROLE;
import com.penguinshop.impl.CustomUserServiceImpl;
import com.penguinshop.model.Cart;
import com.penguinshop.model.LoginRequest;
import com.penguinshop.model.Seller;
import com.penguinshop.model.User;
import com.penguinshop.model.VerificationCode;
import com.penguinshop.repository.CartRepository;
import com.penguinshop.repository.SellerRepository;
import com.penguinshop.repository.UserRepository;
import com.penguinshop.repository.VerificationCodeRepository;
import com.penguinshop.response.AuthResponse;
import com.penguinshop.response.SignupRequest;
import com.penguinshop.service.AuthService;
import com.penguinshop.service.EmailService;
import com.penguinshop.service.OtpService;
import com.penguinshop.utils.OtpUtil;

import lombok.RequiredArgsConstructor;

/**
 * <p>
 * Implementation of {@code com.penguinshop.service.AuthService},
 * </p>
 * 
 */

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;
    private final CartRepository cartRepository;
    private final VerificationCodeRepository verificationCodeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final EmailService emailService;
    private final CustomUserServiceImpl customUserService;
    private final OtpService otpService;

    @Override
    public void sendOtp(String email, USER_ROLE role) throws Exception {
        String SIGNIN_PREFIX = "signin_";

        System.out.println("EMAIL IS - " + email);
        if (email.startsWith(SIGNIN_PREFIX)) {
            email = email.substring(SIGNIN_PREFIX.length());

            System.out.println("EMAIL IS - " + email);

            if (role == null) {
                System.out.println("From Asimpl: Role cannot be empty! Role - " + role);
                throw new Exception("Role cannot be empty!");
            }

            if (role.equals(USER_ROLE.ROLE_CUSTOMER)) {
                User user = userRepository.findByEmail(email);

                if (user == null) {
                    throw new Exception("User does not exist with email - " + email);
                }
            } else if (role.equals(USER_ROLE.ROLE_SELLER)) {
                Seller seller = sellerRepository.findByEmail(email);
                if (seller == null) {
                    throw new Exception("Seller does not exist with email - " + email);
                }
            }

            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new Exception("User does not exist for this email");
            }
        }

        String otp = otpService.generateOtp(email);

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(email);

        // Save in database
        verificationCodeRepository.save(verificationCode);

        String subject = "Penguin Shop login/signup OTP";
        String text = "Your login/signup OTP is - " + otp;

        System.out.println("\n\nI'M ABOUT TO SEND THE OTP, THE CONTENTS ARE: " + text);
        // Send email to user
        emailService.sendVerificationOtpEmail(email, otp, subject, text);
        System.out.printf("I HAVE SENT THE OTP\n\n");
    }

    @Override
    public String createUser(SignupRequest req) throws Exception {

        // get vCode using email
        VerificationCode verificationCode = verificationCodeRepository.findByEmail(req.getEmail());

        System.out.println("Code: " + verificationCode.getOtp());
        if (verificationCode == null || !verificationCode.getOtp().equals(req.getOtp())) {
            throw new Exception("No such OTP for this email address");
        }

        User user = userRepository.findByEmail(req.getEmail());

        if (user == null) {
            User createdUser = new User();
            createdUser.setEmail(req.getEmail());
            createdUser.setFullName(req.getFullName());
            createdUser.setRole(USER_ROLE.ROLE_CUSTOMER);
            createdUser.setMobile("896734531");
            createdUser.setPassword(passwordEncoder.encode(req.getOtp()));

            user = userRepository.save(createdUser);

            // Whenever a new user is created, they must be assigned a new cart
            Cart cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(req.getEmail(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtProvider.generateToken(authentication);
    }

    @Override
    public AuthResponse signIn(LoginRequest req) {
        String email = req.getEmail();
        String otp = req.getOtp();

        Authentication authentication = authenticate(email, otp);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();

        // Get user role from authentication
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();

        authResponse.setJwt(jwt);
        authResponse.setMessage("Login success");
        authResponse.setRole(USER_ROLE.valueOf(role));

        return authResponse;
    }

    private Authentication authenticate(String username, String otp) {
        String SELLER_PREFIX = "seller_";
        UserDetails userDetails = customUserService.loadUserByUsername(username);
        
        // Check if username/password are valid
        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username or password");
        }

        if (username.startsWith(SELLER_PREFIX)) {
            username = username.substring(SELLER_PREFIX.length());
        }

        VerificationCode verificationCode = verificationCodeRepository.findByEmail(username);

        // Check if OTP exists and if it is valid
        if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
            System.out.println("THE BAD OTP IS - " + otp);
            throw new BadCredentialsException("Wrong OTP!");
        }

        // As OTP is valid, delete it from database so it can only be used once
        verificationCodeRepository.delete(verificationCode);

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}
