package com.penguinshop.service;

import org.springframework.stereotype.Service;

import com.penguinshop.model.Product;
import com.penguinshop.model.User;
import com.penguinshop.model.Wishlist;

@Service
public interface WishlistService{
    Wishlist getWishlistByUser(User user);
    Wishlist createWishlist(User user);
    Wishlist addProductToWishlist(User user, Product product);
}
