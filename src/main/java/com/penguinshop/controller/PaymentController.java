package com.penguinshop.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.penguinshop.model.Order;
import com.penguinshop.model.PaymentOrder;
import com.penguinshop.model.Seller;
import com.penguinshop.model.SellerReport;
import com.penguinshop.model.User;
import com.penguinshop.response.ApiResponse;
import com.penguinshop.response.PaymentLinkResponse;
import com.penguinshop.service.PaymentService;
import com.penguinshop.service.SellerReportService;
import com.penguinshop.service.SellerService;
import com.penguinshop.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    private final PaymentService paymentService;
    private final UserService userService;
    private final SellerService sellerService;
    private final SellerReportService sellerReportService;

    @GetMapping("/{paymentId}")
    public ResponseEntity<ApiResponse> paymentSuccessHandler(
            @PathVariable String paymentId,
            @RequestParam String paymentLinkId,
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);

        PaymentLinkResponse paymentLinkResponse;

        PaymentOrder paymentOrder = paymentService.getPaymentOrderByPaymentLinkId(paymentId);

        boolean isPaymentSuccessful = paymentService.proceedPaymentOrder(paymentOrder, paymentId, paymentLinkId);

        if(isPaymentSuccessful) {
            for(Order order : paymentOrder.getOrders()) {
                Seller seller = sellerService.getSellerById(order.getSellerId());
                SellerReport sellerReport = sellerReportService.getSellerReport(seller);
                sellerReport.setTotalOrders(sellerReport.getTotalOrders() + 1);
                sellerReport.setTotalEarnings(sellerReport.getTotalEarnings() + order.getTotalSellingPrice());
                sellerReport.setTotalSales(sellerReport.getTotalSales() + order.getOrderItems().size());
                sellerReportService.updateSellerReport(sellerReport);
            }
        }
        ApiResponse res = new ApiResponse();
        res.setMessage("Payment successful");

        return new ResponseEntity<>(res, HttpStatus.CREATED)
    }
}
