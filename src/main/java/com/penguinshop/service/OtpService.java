package com.penguinshop.service;

import org.springframework.stereotype.Service;

import com.penguinshop.repository.VerificationCodeRepository;

import lombok.RequiredArgsConstructor;

@Service
public interface OtpService{
    String generateOtp(String email);
}
