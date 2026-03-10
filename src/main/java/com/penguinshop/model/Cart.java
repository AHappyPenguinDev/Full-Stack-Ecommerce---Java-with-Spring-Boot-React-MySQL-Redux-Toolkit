package com.penguinshop.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a cart in the application.
 *
 * <p>This entity is mapped to a database table using JPA and stores
 * the following information about carts: 
 * <ul>
 *  <li>User
 *  <li>Cart items
 *  <li>Total selling price
 *  <li>Total items
 *  <li>Total maximum retail price
 *  <li>Discount amount
 *  <li>Coupon code
 * </ul>
 */

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    // One user can only have One cart
    // Which user this cart belongs to
    @OneToOne 
    private User user;

    //Cart contains multiple cartItems
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true) //Cascade means changes to CartItem will be reflected in this 
    private Set<CartItem> cartItems = new HashSet<>();

    private double totalSellingPrice;
    
    private int totalItems;

    private int totalMrpPrice; // Maximum retail price for product in India and Indonesia
                            
    private int discount;

    private String couponCode; //If user has any coupon code applided
}
