package com.penguinshop.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.penguinshop.model.Cart;
import com.penguinshop.model.Coupon;
import com.penguinshop.model.User;
import com.penguinshop.response.ApiResponse;
import com.penguinshop.service.CouponService;
import com.penguinshop.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupons")
public class CouponController {
    private final CouponService couponService;
    private final UserService userService;


    @GetMapping("/{id}")
    public ResponseEntity<Coupon> findCouponById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(couponService.findCouponById(id));
    }

    @PostMapping("/apply")
    public ResponseEntity<Cart> applyCoupon(@RequestHeader("Authorization") String jwt, @RequestParam String code,
            @RequestParam double orderValue, @RequestParam String apply)
            throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Cart cart = couponService.applyCoupon(code, user, orderValue);

        // In the video he does this but I don't like it
        // Cart cart;
        // if (apply.equals("true")) {
        //     cart = couponService.applyCoupon(code, user, orderValue);
        // } else {
        //     cart = couponService.removeCoupon(code, user);
        // }

        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Cart> removeCoupon(@RequestParam String code, @RequestHeader("Authorization") String jwt)
            throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Cart cart = couponService.removeCoupon(code, user);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/admin/create")
    public ResponseEntity<Coupon> createCoupon(@RequestBody Coupon coupon) {
        Coupon createdCoupon = couponService.createCoupon(coupon);
        return new ResponseEntity<>(createdCoupon, HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<ApiResponse> deleteCoupon(@PathVariable Long couponId) throws Exception {
        return ResponseEntity.ok(couponService.deleteCoupon(couponId));
    }
    
    @GetMapping("/admin/all")
    public ResponseEntity<List<Coupon>> findAllCoupons() {
        return ResponseEntity.ok(couponService.findAllCoupons());
    }
}
