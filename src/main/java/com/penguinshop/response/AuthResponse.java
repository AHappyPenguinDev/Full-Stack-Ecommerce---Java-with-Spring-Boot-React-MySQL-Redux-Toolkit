package com.penguinshop.response;

import com.penguinshop.domain.USER_ROLE;

import lombok.Data;

/** <p>The response returned after a successful authentication
 *this contains:</p>
 * <ul>
 * <li> Jwt token
 * <li> Message with registering status
 * <li> Role of created user
 * </ul>
 */

@Data
public class AuthResponse{
    private String jwt;
    private String message;
    private USER_ROLE role;
}
