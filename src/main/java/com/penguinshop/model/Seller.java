package com.penguinshop.model;

import com.penguinshop.domain.ACCOUNT_STATUS;
import com.penguinshop.domain.USER_ROLE;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode

public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String sellerName;
    private String mobile;
    
    @Column(unique = true)
    @NotNull
    private String email;
    private String password;

    @Embedded
    private BusinessDetails businessDetails = new BusinessDetails();

    @Embedded
    private BankDetails bankDetails = new BankDetails();

    @OneToOne(cascade = CascadeType.ALL)
    private Address pickupAddress;

    private String GSTIN; // Goods and services tax identification number

    private USER_ROLE role = USER_ROLE.ROLE_SELLER;

    private boolean isEmailVerified = false;

    private ACCOUNT_STATUS accountStatus =  ACCOUNT_STATUS.PENDING_VERIFICATION;
}
