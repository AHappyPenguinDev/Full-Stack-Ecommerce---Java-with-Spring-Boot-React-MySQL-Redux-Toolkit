package com.penguinshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.penguinshop.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    List<Product> findBySellerId(Long id);


// -- Query Explanation
// SELECT p FROM Product p** → “Give me product items”
// WHERE** → “But only these ones:”
// :query IS NULL** → “Is the search box empty?”
// OR** → “If the search box is empty, return everything. Otherwise, check the next condition.”
// LOWER(p.title)** → “Look at the product name and make it lowercase.”
// LIKE** → “Does it contain…”
// CONCAT('%', :query, '%')** → “…your search word wrapped in % signs so it can appear anywhere.”
// LOWER(...)** → “Also make the search word lowercase so the match ignores uppercase/lowercase.”
// OR** → “If the product name didn’t match, try another field.”
// p.category.categoryId** → “Look at the product’s category ID/name instead.”
// LOWER(p.category.categoryId)** → “Make the category name lowercase before comparing.”
// LIKE CONCAT('%', :query, '%')** → “Check if the category name contains the search word anywhere.”

    @Query("SELECT p FROM Product p WHERE :query IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.category.categoryId) LIKE LOWER(CONCAT('%', :query, '%'))")
    //@Param associates the method parameter with the query's named parameter (":query")
    List<Product> searchProduct(@Param("query") String query);
}
