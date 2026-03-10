package com.penguinshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.penguinshop.model.Cart;
import com.penguinshop.model.CartItem;
import com.penguinshop.model.Product;

public interface CartItemRepository extends JpaRepository<CartItem, Long>{
    CartItem findByCartAndProductAndSize(Cart cart, Product product, String size);
}
