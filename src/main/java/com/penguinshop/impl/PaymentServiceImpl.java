package com.penguinshop.impl;

import java.util.Set;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.penguinshop.domain.PAYMENT_ORDER_STATUS;
import com.penguinshop.domain.PAYMENT_STATUS;
import com.penguinshop.model.Order;
import com.penguinshop.model.PaymentOrder;
import com.penguinshop.model.User;
import com.penguinshop.repository.OrderRepository;
import com.penguinshop.repository.PaymentOrderRepository;
import com.penguinshop.service.PaymentService;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    PaymentOrderRepository paymentOrderRepository;
    OrderRepository orderRepository;

    @Value("${razorpay.api.key}")
    private String apiKey = "apiKey";
    @Value("${razorpay.api.secret}")
    private String apiSecret = "apiSecret";
    @Value("${stripe.secret.key}")
    private String stripeSecretKey = "stripSecretKey";

    @Override
    public PaymentOrder createOrder(User user, Set<Order> orders) {
        Long amount = orders.stream().mapToLong(Order::getTotalSellingPrice).sum();
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setAmount(amount);
        paymentOrder.setUser(user);
        paymentOrder.setOrders(orders);
        return paymentOrderRepository.save(paymentOrder);
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long orderId) throws Exception {
        return paymentOrderRepository.findById(orderId)
                .orElseThrow(() -> new Exception("Payment order not found"));
    }

    @Override
    public PaymentOrder getPaymentOrderByPaymentLinkId(String paymentLinkId) throws Exception {
        return paymentOrderRepository.findByPaymentLinkId(paymentLinkId)
                .orElseThrow(() -> new Exception("Payment order not found"));
    }

    @Override
    public Boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId, String paymentLinkId)
            throws RazorpayException {

        // Check if payment is done or not
        if (paymentOrder.getStatus().equals(PAYMENT_ORDER_STATUS.PENDING)) {
            RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);
            Payment payment = razorpay.payments.fetch(paymentId);
            String status = payment.get("status");

            // Captured means order is done
            if (status.equals("captured")) {
                Set<Order> orders = paymentOrder.getOrders();
                for (Order order : orders) {
                    order.setPaymentStatus(PAYMENT_STATUS.COMPLETED);
                    orderRepository.save(order);
                }

                paymentOrder.setStatus(PAYMENT_ORDER_STATUS.SUCCESS);
                paymentOrderRepository.save(paymentOrder);
                return true;
            }

            // If order is not captured then it has failed
            paymentOrder.setStatus(PAYMENT_ORDER_STATUS.FAILED);
            paymentOrderRepository.save(paymentOrder);
            return false;
        }
        return false;
    }

    @Override
    public PaymentLink createRazorpayPaymentLink(User user, Long amount, Long orderId) throws RazorpayException {
        amount = amount * 100; // Amount in pesa
        try {
            RazorpayClient razorpayClient = new RazorpayClient(apiKey, apiSecret);
            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount", amount);
            paymentLinkRequest.put("currency", "INR");

            JSONObject customer = new JSONObject();
            customer.put("name", user.getFullName());
            customer.put("email", user.getEmail());
            paymentLinkRequest.put("customer", customer);

            JSONObject notify = new JSONObject();
            // could be sms
            notify.put("email", true);
            paymentLinkRequest.put("notify", notify);

            paymentLinkRequest.put("callback_url", "http://localhost:3000/payment-success/" + orderId);
            paymentLinkRequest.put("callback_method", "get");

            PaymentLink paymentLink = razorpayClient.paymentLink.create(paymentLinkRequest);

            String paymentLinkUrl = paymentLink.get("short_url");
            String paymentLinkId = paymentLink.get("id");

            return paymentLink;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RazorpayException(e.getMessage());
        }
    }

    @Override
    public String createStripePaymentLink(User user, Long amount, Long orderId) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:3000/payment-success/")
                .setCancelUrl("http://localhost:3000/payment-cancel/")
                .addLineItem(SessionCreateParams.LineItem.builder().setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount(amount * 100)
                                .setProductData(
                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                .setName("penguin shop payment")
                                                .build())
                                .build())
                        .build())
                .build();
        Session session = Session.create(params);
        
        // User can make payment with this url
        return session.getUrl();
    }

}
