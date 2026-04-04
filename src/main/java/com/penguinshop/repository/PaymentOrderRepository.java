package com.penguinshop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.penguinshop.model.PaymentOrder;

public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Long>{
    Optional<PaymentOrder> findByPaymentLinkId(String paymentLinkId);
}
