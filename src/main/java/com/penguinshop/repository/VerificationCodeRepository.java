package com.penguinshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.penguinshop.model.VerificationCode;

/**
 * <p>
 * Repository interface for performing CRUD operations on
 * {@code com.penguinshop.model.VerificationCode}<br>
 * This repository also includes a {@link #findByEmail(String)}
 * method for retrieving a verification code by its email
 * </p>
 */

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    VerificationCode findByEmail(String email);
    VerificationCode findByOtp(String otp);
}
