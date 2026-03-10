package com.penguinshop.impl;

import org.springframework.stereotype.Service;

import com.penguinshop.model.CartItem;
import com.penguinshop.model.User;
import com.penguinshop.repository.CartItemRepository;
import com.penguinshop.service.CartItemService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;

    @Override
    public CartItem updateCartItem(Long userId, Long cartItemId, CartItem cartItem) throws Exception {
        CartItem item = findCartItemById(cartItemId);

        User cartItemUser = item.getCart().getUser();

        // This check does not allow a user to change someone else's cart
        if (cartItemUser.getId().equals(userId)) {
            item.setQuantity(cartItem.getQuantity());
            item.setMrpPrice(item.getQuantity() * item.getProduct().getMrpPrice());
            item.setSellingPrice(item.getQuantity() * item.getProduct().getSellingPrice());
            return cartItemRepository.save(item);
        }
        throw new Exception("You can't update this cart item");
    }

    @Override
    public void removeCartItem(Long userId, Long cartItemId) throws Exception {
        CartItem item = findCartItemById(cartItemId);
        User cartItemUser = item.getCart().getUser();
        
        if(cartItemUser.getId().equals(userId)) {
            cartItemRepository.delete(item);
        } else{
            throw new Exception("You can't delete this item");
        }
    }

    @Override
    public CartItem findCartItemById(Long cartItemId) throws Exception {
        return cartItemRepository.findById(cartItemId).orElseThrow(() -> 
                new Exception("Cart item not found with ID - " + cartItemId));
    }

}
