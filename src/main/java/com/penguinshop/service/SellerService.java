package com.penguinshop.service;

import java.util.List;

import com.penguinshop.domain.ACCOUNT_STATUS;
import com.penguinshop.model.Seller;

public interface SellerService{
    Seller getSellerProfile(String jwt) throws Exception;
    Seller createSeller(Seller seller) throws Exception;
    Seller getSellerById(Long id) throws Exception;
    Seller getSellerByEmail(String email) throws Exception;
    List<Seller> getAllSellers(ACCOUNT_STATUS status); //filter by status
    Seller updateSeller(Long id, Seller seller) throws Exception;                                                   
    void deleteSeller(Long id) throws Exception;
    Seller verifyEmail(String email, String otp) throws Exception;
    Seller updateSellerAccountStatus(Long sellerId, ACCOUNT_STATUS status) throws Exception;
}
