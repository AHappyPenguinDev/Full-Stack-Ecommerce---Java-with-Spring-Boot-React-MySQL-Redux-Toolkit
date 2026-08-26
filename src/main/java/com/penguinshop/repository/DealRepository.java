package com.penguinshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.penguinshop.model.Deal;

public interface DealRepository extends JpaRepository<Deal, Long>{

}
