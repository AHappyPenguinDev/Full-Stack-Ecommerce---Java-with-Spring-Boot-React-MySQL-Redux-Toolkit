package com.penguinshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.penguinshop.model.Cart;

/**
 * <p>Repository interface for performing CRUD operations on
 * {@link com.penguinshop.model.Cart}</p>
 */

// When a new user is created a new cart must be assigned
public interface CartRepository extends JpaRepository<Cart, Long>{

}
