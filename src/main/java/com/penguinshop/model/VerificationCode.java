package com.penguinshop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a verification code in the application.
 *
 * <p>This entity is mapped to a database table using JPA and stores
 * the following information about the code: 
 * <ul>
 *  <li>The one time password
 *  <li>Email
 *  <li>User
 *  <li>Seller
 * </ul>
 */



@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class VerificationCode{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String otp; // One time password when logging in

    private String email;

    @OneToOne
    private User user;

    @OneToOne
    private Seller seller;
}
