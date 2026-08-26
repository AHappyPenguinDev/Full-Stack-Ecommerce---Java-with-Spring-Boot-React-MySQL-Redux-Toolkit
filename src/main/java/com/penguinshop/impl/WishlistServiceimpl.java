package com.penguinshop.impl;

import org.springframework.stereotype.Service;

import com.penguinshop.model.Product;
import com.penguinshop.model.User;
import com.penguinshop.model.Wishlist;
import com.penguinshop.repository.WishlistRepository;
import com.penguinshop.service.WishlistService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WishlistServiceimpl implements WishlistService {
    WishlistRepository wishlistRepository;

    @Override
    public Wishlist getWishlistByUser(User user) {
        Long userId = user.getId();
        Wishlist wishlist = wishlistRepository.findByUserId(userId);

        if (wishlist == null)
            wishlist = createWishlist(user);

        return wishlist;
    }

    @Override
    public Wishlist createWishlist(User user) {
        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        return wishlistRepository.save(wishlist);
    }

    @Override
    public Wishlist addProductToWishlist(User user, Product product) {
        Wishlist wishlist = getWishlistByUser(user);
        
        // Do not add duplicated products to wishlist
        if (wishlist.getProducts().contains(product))
            return wishlist;

        wishlist.getProducts().add(product);
        
        return wishlistRepository.save(wishlist);
    }

}
