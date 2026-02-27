package com.penguinshop.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

/**
 * Service responsible for sending emails.
 * <p>
 * Currently supports sending verification emails containing one-time passwords
 * (OTP).
 */
@Service
@RequiredArgsConstructor
public class EmailService {
    @Autowired
    private final JavaMailSender javaMailSender;

    /**
     * Sends a verification email containing a one-time password (OTP) to the
     * specified user.
     *
     * @param userEmail the recipient's email address
     * @param otp       the one-time password to be included in the email
     * @param subject   the subject line of the email
     * @param text      the body content of the email (plain text or HTML)
     *
     * @throws {@code MailSendException} if the email could not be created or sent
     */

    public void sendVerificationOtpEmail(String userEmail, String otp, String subject, String text) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,
                    "utf-8");
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(text);
            mimeMessageHelper.setTo(userEmail);
            javaMailSender.send(mimeMessage);
        } catch (MailException | MessagingException e) {
            throw new MailSendException("Failed to send E-mail: ", e);
        }
    }
}
