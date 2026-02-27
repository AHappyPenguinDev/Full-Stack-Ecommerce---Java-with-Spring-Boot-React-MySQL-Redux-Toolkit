
package com.penguinshop.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.penguinshop.domain.USER_ROLE;
import com.penguinshop.model.Seller;
import com.penguinshop.model.User;
import com.penguinshop.repository.SellerRepository;
import com.penguinshop.repository.UserRepository;

import lombok.RequiredArgsConstructor;
/**
 * Custom implementation of {@code UserDetailsService}
 * used by Spring Security to load user-specific data during authentication.
 *
 * <p>
 * This service supports two types of principals:
 * </p>
 * <ul>
 *     <li><b>Sellers</b> – identified by a username prefixed with {@code "seller_"}.</li>
 *     <li><b>Customers</b> – identified by their email address.</li>
 * </ul>
 *
 * <p>
 * Based on the username format, the service determines which repository to query
 * and constructs a {@code UserDetails}
 * object with the appropriate role and authorities.
 * </p>
 *
 * <p>
 * If no matching user or seller is found, a {@code UsernameNotFoundException} is thrown.
 * </p>
 */
@RequiredArgsConstructor
@Service
public class CustomUserServiceImpl implements UserDetailsService {

    private final SellerRepository sellerRepository;
    private final UserRepository userRepository;
    private static final String SELLER_PREFIX = "seller_";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.startsWith(SELLER_PREFIX)) {
            String actualUsername = username.substring(SELLER_PREFIX.length());
            Seller seller = sellerRepository.findByEmail(actualUsername); 
            // If user is not a seller, email will not be found
            if (seller != null)
                return buildUserDetails(seller.getEmail(), seller.getPassword(), seller.getRole());

        } else {
            User user = userRepository.findByEmail(username);
            if (user != null)
                return buildUserDetails(user.getEmail(), user.getPassword(), user.getRole());

        }
        throw new UsernameNotFoundException("Email for user or seller with Username " + username + " not found.");
    }

    private UserDetails buildUserDetails(String email, String password, USER_ROLE role) {
        if (role == null)
            role = USER_ROLE.ROLE_CUSTOMER;

        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority(role.toString()));

        return new org.springframework.security.core.userdetails.User(
                email, password, authorityList);
    }

}
