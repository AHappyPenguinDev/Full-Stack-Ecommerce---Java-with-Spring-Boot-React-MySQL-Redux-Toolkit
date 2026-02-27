package com.penguinshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.penguinshop.model.User;

/**
 * <p>
 * Repository interface for performing CRUD operations on
 * {@code com.penguinshop.model.User}<br>
 * This repository also includes a {@link #findByEmail(String)} 
 * method for retrieving a user by their email
 * </p>
 */

// JpaRepository<Entity and Id Type>
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
