package com.penguinshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.penguinshop.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    List<Product> findBySellerId(Long id);

    String query = "SELECT p FROM Product p where (:query is null or lower(p.title))" + "LIKE lower(concat('%', :query, '%'))" +
    "or (:query is null or lower(p.category.name)))" +
    "like lower(concat('%', :query, '%'))";
    
    @Query(query)
    List<Product> searchProduct(@Param("query") String query);
}
