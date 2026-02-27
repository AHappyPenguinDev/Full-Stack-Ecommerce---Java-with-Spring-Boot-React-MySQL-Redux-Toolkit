package com.penguinshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.penguinshop.model.Address;


public interface AddressRepository extends JpaRepository<Address, Long> {

}

