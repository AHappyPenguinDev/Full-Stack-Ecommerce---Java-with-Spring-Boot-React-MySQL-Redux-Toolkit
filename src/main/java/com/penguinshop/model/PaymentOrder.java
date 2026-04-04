package com.penguinshop.model;

import java.util.HashSet;
import java.util.Set;

import com.penguinshop.domain.PAYMENT_METHOD;
import com.penguinshop.domain.PAYMENT_ORDER_STATUS;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode

// If orders are from different brands need to create different payment orders
public class PaymentOrder{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private Long amount;

    private PAYMENT_ORDER_STATUS status = PAYMENT_ORDER_STATUS.PENDING;

    private PAYMENT_METHOD paymentMethod;

    private String paymentLinkId;

    @ManyToOne
    private User user;

    @OneToMany
    private Set<Order> orders = new HashSet<>();
}
