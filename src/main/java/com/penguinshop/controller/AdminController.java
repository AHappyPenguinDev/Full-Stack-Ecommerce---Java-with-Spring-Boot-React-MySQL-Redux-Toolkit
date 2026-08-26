package com.penguinshop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.penguinshop.domain.ACCOUNT_STATUS;
import com.penguinshop.model.Seller;
import com.penguinshop.service.SellerService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AdminController{
    private final SellerService sellerService;

    public ResponseEntity<Seller> updateSellerStatus(@PathVariable Long id, @PathVariable ACCOUNT_STATUS status) throws Exception {
        Seller updatedSeller = sellerService.updateSellerAccountStatus(id, status);
        return ResponseEntity.ok(updatedSeller);
    }
}
