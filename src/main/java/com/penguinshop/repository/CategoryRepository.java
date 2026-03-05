package com.penguinshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.penguinshop.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{

    Category findByCategoryId(String categoryId);

}
