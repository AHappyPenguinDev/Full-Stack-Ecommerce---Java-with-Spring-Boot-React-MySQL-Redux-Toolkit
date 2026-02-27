package com.penguinshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.penguinshop.domain.ACCOUNT_STATUS;
import com.penguinshop.model.Seller;

/**
 * <p>
 * Repository interface for performing CRUD operations on
 * {@code com.penguinshop.model.Seller}
 * This repository also includes a {@link #findByEmail(String)} 
 * method for retrieving a seller by their email
 * </p>
 */

public interface SellerRepository extends JpaRepository<Seller, Long> {
    Seller findByEmail(String email);
    List<Seller> findAllByAccountStatus(ACCOUNT_STATUS accountStatus);
}
