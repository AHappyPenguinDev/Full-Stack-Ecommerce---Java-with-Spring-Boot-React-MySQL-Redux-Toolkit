package com.penguinshop.impl;

import org.springframework.stereotype.Service;

import com.penguinshop.model.Cart;
import com.penguinshop.model.CartItem;
import com.penguinshop.model.Product;
import com.penguinshop.model.User;
import com.penguinshop.repository.CartItemRepository;
import com.penguinshop.repository.CartRepository;
import com.penguinshop.service.CartService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public CartItem addCartItem(User user, Product product, String size, int quantity) {
        Cart cart = findUserCart(user);

        CartItem isPresent = cartItemRepository.findByCartAndProductAndSize(cart, product, size);

        if (isPresent == null) {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUserId(user.getId());
            cartItem.setSize(size);
            cartItem.setCart(cart);

            int mrpTotal = quantity * product.getMrpPrice();
            cartItem.setMrpPrice(mrpTotal);

            int sellingTotal = quantity * product.getSellingPrice();
            cartItem.setSellingPrice(sellingTotal);

            cart.getCartItems().add(cartItem);
            cartItem.setCart(cart);

            return cartItemRepository.save(cartItem);
        }

        return isPresent;
    }

    @Override
    public Cart findUserCart(User user) {
        Cart cart = cartRepository.findByUserId(user.getId());

        int totalPrice = 0;
        int totalDiscountedPrice = 0;
        int totalItems = 0;

        for (CartItem cartItem : cart.getCartItems()) {
            totalPrice += cartItem.getMrpPrice();
            totalDiscountedPrice += cartItem.getSellingPrice();
            totalItems += cartItem.getQuantity();
        }

        cart.setTotalMrpPrice(totalPrice);
        cart.setTotalSellingPrice(totalDiscountedPrice);
        cart.setDiscount(calculateDiscountPercentage(totalPrice, totalDiscountedPrice));
        cart.setTotalItems(totalItems);
        return cart;
    }

    // Helper method to calculate discount percentage
    private int calculateDiscountPercentage(int mrpPrice, int sellingPrice) {

        // Validate that MRP is greater than zero
        if (mrpPrice <= 0) 
            return 0;

        // Calculate discount amount
        double discount = mrpPrice - sellingPrice;

        // Calculate discount percentage
        double discountPercentage = (discount / mrpPrice) * 100;

        // Convert to integer and return
        return (int) discountPercentage;
    }

}
