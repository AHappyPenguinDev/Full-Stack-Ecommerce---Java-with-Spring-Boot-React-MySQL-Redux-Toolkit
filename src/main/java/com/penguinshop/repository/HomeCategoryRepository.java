package com.penguinshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.penguinshop.model.HomeCategory;

public interface HomeCategoryRepository extends JpaRepository<HomeCategory, Long>{

}
