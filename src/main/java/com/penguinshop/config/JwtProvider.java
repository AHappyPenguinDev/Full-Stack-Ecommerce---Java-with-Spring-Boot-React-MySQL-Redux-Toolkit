package com.penguinshop.config;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * Service responsible for creating and parsing JSON Web Tokens (JWT).
 *
 * <p>
 * This class generates signed JWTs for authenticated users and
 * extracts user information from existing JWTs. Tokens created by
 * this provider include the user's email and granted authorities
 * and are signed using a shared secret key.
 * </p>
 *
 * <p>
 * The generated JWTs have a fixed expiration time and are intended
 * to be used for stateless authentication in Spring Security.
 * </p>
 */
@Service
public class JwtProvider {
    SecretKey key = Keys.hmacShaKeyFor(JWT_CONSTANT.SECRET_KEY.getBytes());

    /**
     * Generates a signed JWT token for an authenticated user.
     *
     * <p>
     * The token includes:
     * </p>
     * <ul>
     * <li>The user's email (as the subject)</li>
     * <li>The user's granted authorities</li>
     * <li>An issued-at timestamp</li>
     * <li>An expiration time of 24 hours</li>
     * </ul>
     *
     * @param auth the authenticated user provided by Spring Security
     * @return a signed JWT token string
     */

    public String generateToken(Authentication auth) {
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        String roles = populateAuthorities(authorities);

        // Create token that expires after 24 hours
        // And put user info into it
        return Jwts.builder()
                .setIssuedAt(new Date()) // Record when token was created
                .setExpiration(new Date(new Date().getTime() + 86400000)) // Expire after 24 hours
                .claim("email", auth.getName()) // Put user email into token
                .claim("authorities", roles) // Put user roles into token
                .signWith(key) // Sign token to prevent tampering
                .compact(); // Build token into a *compact* string
    }

    // Takes a token and returns email extracted from it
    public String getEmailFromJwtToken(String jwt) {
        jwt = jwt.substring(7);
        // Check token hasn't been altered or expired and decodes data inside
        Claims claims = Jwts.parser().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
        String email = String.valueOf(claims.get("email"));

        return email;
    }

    // Convert roles into a comma-separated string
    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<String> auths = new HashSet<>();

        for (GrantedAuthority authority : authorities) {
            auths.add(authority.getAuthority());
        }
        return String.join(",", auths);
    }

}
