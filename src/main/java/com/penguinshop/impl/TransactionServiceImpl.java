package com.penguinshop.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.penguinshop.exceptions.SellerException;
import com.penguinshop.model.Order;
import com.penguinshop.model.Seller;
import com.penguinshop.model.Transaction;
import com.penguinshop.model.User;
import com.penguinshop.repository.TransactionRepository;
import com.penguinshop.service.SellerService;
import com.penguinshop.service.TransactionService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {
    TransactionRepository transactionRepository;
    SellerService sellerService;

    @Override
    public Transaction createTransaction(Order order) throws SellerException {
        User customer = order.getUser();
        Long sellerId = order.getSellerId();
        Seller seller = sellerService.getSellerById(sellerId);

        Transaction transaction = new Transaction();
        transaction.setCustomer(customer);
        transaction.setSeller(seller);
        transaction.setOrder(order);

        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getTransactionsBySeller(Seller seller) throws Exception {
        return transactionRepository.findBySellerId(seller.getId());
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

}
