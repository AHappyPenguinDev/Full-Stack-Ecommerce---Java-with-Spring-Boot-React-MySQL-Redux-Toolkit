package com.penguinshop.service;

import com.penguinshop.model.Cart;
import com.penguinshop.model.CartItem;
import com.penguinshop.model.Product;
import com.penguinshop.model.User;

public interface CartService{
    public CartItem addCartItem(User user, Product product, String size, int quantity);
    public Cart findUserCart(User user);
}
