package com.penguinshop.controller;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.penguinshop.domain.PAYMENT_METHOD;
import com.penguinshop.model.Address;
import com.penguinshop.model.Cart;
import com.penguinshop.model.Order;
import com.penguinshop.model.OrderItem;
import com.penguinshop.model.PaymentOrder;
import com.penguinshop.model.Seller;
import com.penguinshop.model.SellerReport;
import com.penguinshop.model.User;
import com.penguinshop.repository.PaymentOrderRepository;
import com.penguinshop.response.PaymentLinkResponse;
import com.penguinshop.service.CartService;
import com.penguinshop.service.OrderService;
import com.penguinshop.service.PaymentService;
import com.penguinshop.service.SellerReportService;
import com.penguinshop.service.SellerService;
import com.penguinshop.service.UserService;
import com.razorpay.PaymentLink;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;
    private final CartService cartService;
    private final PaymentService paymentService;
    private final SellerService sellerService;
    private final SellerReportService sellerReportService;
    private final PaymentOrderRepository paymentOrderRepository;

    // UNFINISHED -- check before 10:41:47 in the video
    @PostMapping
    public ResponseEntity<PaymentLinkResponse> createOrderHandler(
            @RequestBody Address shippingAddress,
            @RequestParam PAYMENT_METHOD paymentMethod,
            @RequestHeader("Authorization") String jwt)
            throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Cart cart = cartService.findUserCart(user);
        Set<Order> orders = orderService.createOrder(user, shippingAddress, cart);

        PaymentOrder paymentOrder = paymentService.createOrder(user, orders);

        PaymentLinkResponse paymentLinkResponse = new PaymentLinkResponse();

        if (paymentMethod.equals(PAYMENT_METHOD.RAZORPAY)) {
            PaymentLink payment = paymentService.createRazorpayPaymentLink(user,
                    paymentOrder.getAmount(),
                    paymentOrder.getId());
            String paymentUrl = payment.get("short_url");
            String paymentUrlId = payment.get("id");

            paymentOrder.setPaymentLinkId(paymentUrlId);
            paymentOrderRepository.save(paymentOrder);

            paymentLinkResponse.setPaymentLinkUrl(paymentUrl);
        } else if (paymentMethod.equals(PAYMENT_METHOD.STRIPE)) {
            String paymentUrl = paymentService.createStripePaymentLink(user, paymentOrder.getAmount(),
                    paymentOrder.getId());
            paymentLinkResponse.setPaymentLinkUrl(paymentUrl);
        }
        return ResponseEntity.ok(paymentLinkResponse);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Order>> usersOrderHistoryHandler(
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        List<Order> orders = orderService.userOrderHistory(user.getId());
        return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt)
            throws Exception {
        Order order = orderService.findOrderById(orderId);
        return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{orderItemId}")
    public ResponseEntity<OrderItem> getOrderItemById(
            @PathVariable Long orderItemId, @RequestHeader("Authorization") String jwt) throws Exception {
        OrderItem orderItem = orderService.findOrderItemById(orderItemId);
        return new ResponseEntity<>(orderItem, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancelOrder(
            @PathVariable Long orderId,
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Order order = orderService.cancelOrder(orderId, user);

        Seller seller = sellerService.getSellerById(order.getSellerId());
        SellerReport report = sellerReportService.getSellerReport(seller);

        report.setCanceledOrders(report.getCanceledOrders() + 1);
        report.setTotalRefunds(report.getTotalRefunds() + order.getTotalSellingPrice());
        sellerReportService.updateSellerReport(report);

        return ResponseEntity.ok(order);
    }

}
