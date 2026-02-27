package com.penguinshop.response;

import lombok.Data;

/**
 * <p>This class defines the fields required for basic
 * user identification during the signup process:</p>
 * <ul>
     * <li> Email
     * <li> Full name
     * <li> One time password
 * </ul>
 */
@Data
public class SignupRequest{
    private String email;
    private String fullName;
    private String otp;
}
