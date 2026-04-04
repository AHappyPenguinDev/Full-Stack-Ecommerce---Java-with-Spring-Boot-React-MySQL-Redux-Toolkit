package com.penguinshop.service;

import java.util.List;
import java.util.Set;

import com.penguinshop.domain.ORDER_STATUS;
import com.penguinshop.model.Address;
import com.penguinshop.model.Cart;
import com.penguinshop.model.Order;
import com.penguinshop.model.OrderItem;
import com.penguinshop.model.User;

public interface OrderService{
    Set<Order> createOrder(User user, Address shippingAddress, Cart cart);
    Order findOrderById(Long id) throws Exception;
    List<Order> userOrderHistory(Long userId);   
    List<Order> sellerOrders(Long sellerId);
    //If seller wants to update the order status
    Order updateOrderStatus(Long orderId, ORDER_STATUS orderStatus) throws Exception;
    Order cancelOrder(Long orderId, User user) throws Exception;
    OrderItem findOrderItemById(Long id) throws Exception;
}
