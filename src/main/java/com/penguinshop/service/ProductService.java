package com.penguinshop.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.penguinshop.model.Product;
import com.penguinshop.model.Seller;
import com.penguinshop.request.CreateProductRequest;

public interface ProductService {
    public Product createProduct(CreateProductRequest req, Seller seller);

    public void deleteProduct(Long productId);

    public void updateProduct(Long productId, Product product);

    public Product findProductById(Long productId);

    public List<Product> searchProducts();

    // Arguments are filters which the user can filter by
    public Page<Product> getAllProducts(String category, String brand, String colors, String sizes, Integer minPrice,
            Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber);

    // Retrieve all products associated with a seller so they can be display in the seller dashboard
    public List<Product> getProductBySellerId(Long sellerId);
}
