package com.penguinshop.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.penguinshop.domain.USER_ROLE;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a user in the application.
 *
 * <p>This entity is mapped to a database table using JPA and stores
 * the following information about users: 
 * <ul>
 *  <li>Password
 *  <li>Email
 *  <li>Full name
 *  <li>Phone number
 *  <li>Role
 *  <li>Addresses
 * </ul>
 */

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Delegate task to generate ID to springboot
    private Long id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Only write so password can't be fetched from frontend
    private String password;

    // There can only be one user per email
    @Column(unique = true, nullable = false)
    private String email;

    private String fullName;

    private String mobile;

    private USER_ROLE role = USER_ROLE.ROLE_CUSTOMER;

    @OneToMany
    private Set<Address> addresses = new HashSet<>();

    
    @ManyToMany
    @JsonIgnore //When we fetch data from frontEnd this will not be provided as we don't need it
    private Set<Coupon> usedCoupons = new HashSet<>();
}
