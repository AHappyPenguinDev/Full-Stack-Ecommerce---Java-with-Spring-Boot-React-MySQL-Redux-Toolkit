package com.penguinshop.service;

import java.util.List;

import com.penguinshop.exceptions.SellerException;
import com.penguinshop.model.Order;
import com.penguinshop.model.Seller;
import com.penguinshop.model.Transaction;

public interface TransactionService {
    Transaction createTransaction(Order order) throws SellerException;
    List<Transaction> getTransactionsBySeller(Seller seller) throws Exception;
    List<Transaction> getAllTransactions();
}
