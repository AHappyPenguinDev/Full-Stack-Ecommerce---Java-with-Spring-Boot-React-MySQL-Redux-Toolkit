package com.penguinshop.impl;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.penguinshop.config.JwtProvider;
import com.penguinshop.domain.ACCOUNT_STATUS;
import com.penguinshop.domain.USER_ROLE;
import com.penguinshop.model.Address;
import com.penguinshop.model.BankDetails;
import com.penguinshop.model.BusinessDetails;
import com.penguinshop.model.Seller;
import com.penguinshop.repository.AddressRepository;
import com.penguinshop.repository.SellerRepository;
import com.penguinshop.service.SellerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;

    /**
     * Returns seller by jwt token
     */
    
    @Override
    public Seller getSellerProfile(String jwt) throws Exception {
        String email = jwtProvider.getEmailFromJwtToken(jwt);
        return getSellerByEmail(email);
    }

    @Override
    public Seller createSeller(Seller seller) throws Exception {
        Seller sellerExists = sellerRepository.findByEmail(seller.getEmail());
        if (sellerExists != null) {
            throw new Exception("Seller already exists, use a different email!");
        }

        Address savedAddress = addressRepository.save(seller.getPickupAddress());

        // Set fields manually for security, to prevent user from injecting
        // malicious code such as "role" : "ROLE_ADMIN"
        Seller newSeller = new Seller();
        newSeller.setEmail(seller.getEmail());
        newSeller.setPassword(passwordEncoder.encode(seller.getPassword()));
        newSeller.setSellerName(seller.getSellerName());
        newSeller.setPickupAddress(savedAddress);
        newSeller.setGSTIN(seller.getGSTIN());
        newSeller.setRole(USER_ROLE.ROLE_SELLER);
        newSeller.setMobile(seller.getMobile());
        newSeller.setBankDetails(seller.getBankDetails());
        newSeller.setBusinessDetails(seller.getBusinessDetails());

        return sellerRepository.save(newSeller);
    }

    @Override
    public Seller getSellerById(Long id) throws Exception {
        // Must use orElseThrow as seller repo returns an Optional (a seller or an empty Optional)
        return sellerRepository.findById(id)
                .orElseThrow(() -> new Exception("Seller not found with ID - " + id));
    }

    @Override
    public Seller getSellerByEmail(String email) throws Exception {
        Seller seller = sellerRepository.findByEmail(email);
        if (seller == null) {
            throw new Exception("Seller not found with this email: " + email);
        }
        return seller;
    }

    /**
     * Returns all the sellers with the specified status
     * @param @code accountStatus
     */

    @Override
    public List<Seller> getAllSellers(ACCOUNT_STATUS accountStatus) {
        return sellerRepository.findAllByAccountStatus(accountStatus);
    }

    @Override
    public Seller updateSeller(Long id, Seller seller) throws Exception {
        Seller existingSeller = getSellerById(id);

        if (seller.getSellerName() != null) {
            existingSeller.setSellerName(seller.getSellerName());
        }

        if (seller.getMobile() != null) {
            existingSeller.setMobile(seller.getMobile());
        }

        if (seller.getEmail() != null) {
            existingSeller.setEmail(seller.getEmail());
        }

        if (seller.getBusinessDetails() != null && seller.getBusinessDetails().getBusinessName() != null) {
            existingSeller.getBusinessDetails().setBusinessName(seller.getBusinessDetails().getBusinessName());
        }

        if (seller.getBankDetails() != null
                && seller.getBankDetails().getAccountNumber() != null
                && seller.getBankDetails().getIfsCode() != null
                && seller.getBankDetails().getAccountHolderName() != null) {

            existingSeller.getBankDetails().setAccountHolderName(seller.getBankDetails().getAccountHolderName());
            existingSeller.getBankDetails().setAccountNumber(seller.getBankDetails().getAccountNumber());
            existingSeller.getBankDetails().setIfsCode(seller.getBankDetails().getIfsCode());
        }

        if (seller.getPickupAddress() != null
                && seller.getPickupAddress().getAddress() != null
                && seller.getPickupAddress().getMobile() != null
                && seller.getPickupAddress().getState() != null
                && seller.getPickupAddress().getCity() != null) {
            existingSeller.getPickupAddress().setPinCode(seller.getPickupAddress().getPinCode());
            existingSeller.getPickupAddress().setMobile(seller.getPickupAddress().getMobile());
            existingSeller.getPickupAddress().setState(seller.getPickupAddress().getState());
            existingSeller.getPickupAddress().setCity(seller.getPickupAddress().getCity());
        }

        if (seller.getGSTIN() != null) {
            existingSeller.setGSTIN(seller.getGSTIN());
        }

        return sellerRepository.save(existingSeller);
    }

    @Override
    public void deleteSeller(Long id) throws Exception {
        Seller seller = getSellerById(id);
        sellerRepository.delete(seller);
    }

    @Override
    public Seller verifyEmail(String email, String otp) throws Exception {
        Seller seller = getSellerByEmail(email);
        seller.setEmailVerified(true);
        return sellerRepository.save(seller);
    }

    @Override
    public Seller updateSellerAccountStatus(Long sellerId, ACCOUNT_STATUS status) throws Exception {
        Seller seller = getSellerById(sellerId);
        seller.setAccountStatus(status);
        return sellerRepository.save(seller);
    }

}
