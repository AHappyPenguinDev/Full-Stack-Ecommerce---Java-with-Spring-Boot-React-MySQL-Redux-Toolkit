package com.penguinshop.model;

import com.penguinshop.domain.PAYMENT_STATUS;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class PaymentDetails {
    private String paymentId;
    private String razorpayPaymentLinkId;
    private String razorpayPaymentLinkReferenceId;
    private String razorpayPaymentLinkStatus;
    private String razorpayPaymentIdZWSP;
    private PAYMENT_STATUS STATUS;
}
