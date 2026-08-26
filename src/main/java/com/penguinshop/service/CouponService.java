package com.penguinshop.service;

import java.util.List;

import com.penguinshop.model.Cart;
import com.penguinshop.model.Coupon;
import com.penguinshop.model.User;
import com.penguinshop.response.ApiResponse;

public interface CouponService {
    Cart applyCoupon(String code, User user, double orderValue) throws Exception;

    Cart removeCoupon(String code, User user) throws Exception;

    Coupon findCouponById(Long id) throws Exception;

    Coupon createCoupon(Coupon coupon);

    List<Coupon> findAllCoupons();

    ApiResponse deleteCoupon(Long couponId) throws Exception;
}
