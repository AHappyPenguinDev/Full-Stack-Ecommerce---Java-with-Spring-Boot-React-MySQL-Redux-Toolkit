package com.penguinshop.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.penguinshop.model.Cart;
import com.penguinshop.model.Coupon;
import com.penguinshop.model.User;
import com.penguinshop.repository.CartRepository;
import com.penguinshop.repository.CouponRepository;
import com.penguinshop.repository.UserRepository;
import com.penguinshop.response.ApiResponse;
import com.penguinshop.service.CouponService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
    CouponRepository couponRepository;
    CartRepository cartRepository;
    UserRepository userRepository;

    @Override
    public Cart applyCoupon(String code, User user, double orderValue) throws Exception {
        Coupon coupon = couponRepository.findByCode(code);
        Cart cart = cartRepository.findByUserId(user.getId());

        if (coupon == null)
            throw new Exception("Coupon not valid");

        if (user.getUsedCoupons().contains(coupon))
            throw new Exception("Coupon already used");

        double minimumOrderValue = coupon.getMinimumOrderValue();
        if (orderValue < minimumOrderValue)
            throw new Exception("Order must be at least " + minimumOrderValue + " to apply this coupon");

        if (coupon.isActive() &&
                LocalDate.now().isAfter(coupon.getValidityStartDate()) &&
                LocalDate.now().isBefore(coupon.getValidityEndDate())) {
            user.getUsedCoupons().add(coupon);
            userRepository.save(user);

            double discountedPrice = (cart.getTotalSellingPrice() * coupon.getDiscountPercentage()) / 100;

            cart.setTotalSellingPrice(cart.getTotalSellingPrice() - discountedPrice);
            cart.setCouponCode(code);
            cartRepository.save(cart);
            return cart;
        }
        throw new Exception("Coupon not valid");
    }

    @Override
    public Cart removeCoupon(String code, User user) throws Exception {
        Coupon coupon = couponRepository.findByCode(code);
        Cart cart = cartRepository.findByUserId(user.getId());

        if (user.getUsedCoupons().contains(coupon))
            throw new Exception("Coupon already used");

        user.getUsedCoupons().remove(coupon);
        userRepository.save(user);

        // example:
        // 100% - 20% = 80%
        // 80% = 0.80
        // 80/0.80 = 100
        // ORIGINAL PRICE: 100

        double currentPrice = cart.getTotalSellingPrice();
        double currentPercent = (100 - coupon.getDiscountPercentage()) / 100;
        double originalPrice = (currentPrice / currentPercent);

        cart.setTotalSellingPrice(originalPrice);
        cart.setCouponCode(null);
        return cartRepository.save(cart);
    }

    @Override
    public Coupon findCouponById(Long id) throws Exception {
        return couponRepository.findById(id)
                .orElseThrow(() -> new Exception("Coupon not found"));
    }

    // Spring checks if user is ADMIN before executing this method
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Coupon createCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<Coupon> findAllCoupons() {
        return couponRepository.findAll();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse deleteCoupon(Long couponId) throws Exception {
        // Throws exception if not found
        findCouponById(couponId);
        couponRepository.deleteById(couponId);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Coupon deleted successfully");
        return apiResponse;
    }

}
