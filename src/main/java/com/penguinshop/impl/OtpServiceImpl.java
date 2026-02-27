package com.penguinshop.impl;

import java.security.SecureRandom;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.penguinshop.model.VerificationCode;
import com.penguinshop.repository.VerificationCodeRepository;
import com.penguinshop.service.OtpService;

import lombok.RequiredArgsConstructor;

/**
 * Generates a secure random OTP 
 * This service also deletes a previous OTP before creating a new one
 */
@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {
    private final VerificationCodeRepository verificationCodeRepository;

    @Override
    public String generateOtp(String email) {
        int otpLength = 6;

        Random Random = new SecureRandom();

        StringBuilder otp = new StringBuilder(otpLength);

        for (int i = 0; i < otpLength; i++) {
            otp.append(Random.nextInt(10));
        }

        VerificationCode isExist = verificationCodeRepository.findByEmail(email);

        // If a verification code already exists, delete it to create a new one
        if (isExist != null) {
            verificationCodeRepository.delete(isExist);
        }

        System.out.println("GENERATED OTP: " + otp.toString());
        System.out.println(otp);
        return otp.toString();
    }
}
