package com.penguinshop.config;

import java.io.IOException;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Filter runs once per HTTP Request

/**
 * Security filter responsible for validating JSON Web Tokens (JWT)
 * on every incoming HTTP request.
 *
 * <p>
 * This filter extracts a JWT from the configured HTTP header
 * (typically the {@code Authorization} header), verifies its signature
 * using the application's secret key, and parses the token claims.
 * </p>
 *
 * <p>
 * If the token is valid, an
 * {@code org.springframework.security.core.Authentication}
 * object is created using the user information stored in the token
 * (such as email and authorities) and placed into the
 * {@code org.springframework.security.core.context.SecurityContextHolder}.
 * This allows Spring Security to treat the request as authenticated.
 * </p>
 *
 * <p>
 * If the token is invalid, malformed, expired, or has been tampered with,
 * a {@code org.springframework.security.authentication.BadCredentialsException}
 * is thrown and the request is rejected.
 * </p>
 *
 * <p>
 * This filter runs once per request and does not perform authentication
 * when no JWT is present, allowing public endpoints to function normally.
 * </p>
 */

public class JwtTokenValidator extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Reads the JWT from the request Header, e.g: Authorization: Bearer
        // ey10239jlkasdjf09klaj...
        String jwt = request.getHeader(JWT_CONSTANT.JWT_HEADER);

        if (jwt != null) {
            jwt = jwt.substring(7); // Token come as Bearer jwt, so substring extracts only jwt
            try {
                // Create secret key to sign token (key), check that it hasn't been tampered
                // with and decode its data(claims)
                SecretKey key = Keys.hmacShaKeyFor(JWT_CONSTANT.SECRET_KEY.getBytes());
                Claims claims = Jwts.parser().setSigningKey(key).build().parseClaimsJws(jwt).getBody();

                String email = String.valueOf(claims.get("email"));
                String authorities = String.valueOf(claims.get("authorities"));

                // Convert roles into Spring Security format
                List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
                // Create an authenticated user object, user is identified by email and no
                // password
                // is needed as JWT has already proved identity. This user has certain roles
                // (auths)
                Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, auths);

                // Tell spring user is authenticated. Spring Security now treats the user as
                // logged in
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                throw new BadCredentialsException("Invalid JWT token...");
            }
        }

        // Let request continue
        filterChain.doFilter(request, response);
    }

}
