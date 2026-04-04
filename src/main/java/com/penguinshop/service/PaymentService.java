package com.penguinshop.service;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.penguinshop.model.Order;
import com.penguinshop.model.PaymentOrder;
import com.penguinshop.model.User;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;

public interface PaymentService {
    PaymentOrder createOrder(User user, Set<Order> orders);

    PaymentOrder getPaymentOrderById(Long orderId) throws Exception;

    PaymentOrder getPaymentOrderByPaymentLinkId(String paymentId) throws Exception;

    Boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId, String paymentLinkId) throws RazorpayException;

    PaymentLink createRazorpayPaymentLink(User user, Long amount, Long orderId) throws RazorpayException;
    String createStripePaymentLink(User user, Long amount, Long orderId) throws StripeException;
}
