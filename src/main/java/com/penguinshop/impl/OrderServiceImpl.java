package com.penguinshop.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.penguinshop.domain.ORDER_STATUS;
import com.penguinshop.domain.PAYMENT_STATUS;
import com.penguinshop.model.Address;
import com.penguinshop.model.Cart;
import com.penguinshop.model.CartItem;
import com.penguinshop.model.Order;
import com.penguinshop.model.OrderItem;
import com.penguinshop.model.User;
import com.penguinshop.repository.AddressRepository;
import com.penguinshop.repository.OrderItemRepository;
import com.penguinshop.repository.OrderRepository;
import com.penguinshop.service.OrderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public Set<Order> createOrder(User user, Address shippingAddress, Cart cart) {
         
        // Add address if it doesn't exist
        if (!user.getAddresses().contains(shippingAddress))
            user.getAddresses().add(shippingAddress);
        Address address = addressRepository.save(shippingAddress);

        // Creates a list of cartItems grouped by the seller id 
        // Seller 1 : <1, Item[ball,toy sword, pool noodle]>
        // Seller 2 : <2, Items[watch, clock, belt]>
        Map<Long, List<CartItem>> itemsBySeller = cart.getCartItems().stream()
                .collect(Collectors.groupingBy(item -> item.getProduct().getSeller().getId()));
        Set<Order> orders = new HashSet<>();

        for(Map.Entry<Long, List<CartItem>> entry : itemsBySeller.entrySet()) {
            Long sellerId = entry.getKey();
            List<CartItem> items = entry.getValue();

            int totalOrderPrice = items.stream().mapToInt(CartItem::getSellingPrice).sum();
            int totalitems = items.stream().mapToInt(CartItem::getQuantity).sum();

            Order createdOrder = new Order();
            createdOrder.setUser(user);
            createdOrder.setSellerId(sellerId);
            createdOrder.setTotalMrpPrice(totalOrderPrice);
            createdOrder.setTotalItems(totalitems);
            createdOrder.setShippingAddress(shippingAddress);
            createdOrder.setOrderStatus(ORDER_STATUS.PENDING);
            createdOrder.getPaymentDetails().setSTATUS(PAYMENT_STATUS.PENDING);

            Order savedOrder = orderRepository.save(createdOrder);
            orders.add(savedOrder);

            List<OrderItem> orderItems = new ArrayList<>();

            for(CartItem item : items) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(savedOrder);
                orderItem.setMrpPrice(item.getMrpPrice());
                orderItem.setProduct(item.getProduct());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setSize(item.getSize());
                orderItem.setSellingPrice(orderItem.getSellingPrice());
                orderItem.setUserId(item.getUserId());
                savedOrder.getOrderItems().add(orderItem);

                OrderItem savedOrderItem = orderItemRepository.save(orderItem);
                orderItems.add(savedOrderItem);
            }
        }
        
        return orders;

    }

    @Override
    public Order findOrderById(Long id) throws Exception {
        return orderRepository.findById(id).orElseThrow(() -> new Exception("Order not found with this ID"));
    }

    @Override
    public List<Order> userOrderHistory(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public List<Order> sellerOrders(Long sellerId) {
        return orderRepository.findBySellerId(sellerId);
    }

    @Override
    public Order updateOrderStatus(Long orderId, ORDER_STATUS orderStatus) throws Exception {
        Order order = findOrderById(orderId);
        order.setOrderStatus(orderStatus);
        return orderRepository.save(order);
    }

    @Override
    public Order cancelOrder(Long orderId, User user) throws Exception {
        // Check if order belongs to user
        Order order = findOrderById(orderId);
        if(!user.getId().equals(order.getUser().getId()))
            throw new Exception("You do not have access to this order");
        // Cancel order
        order.setOrderStatus(ORDER_STATUS.CANCELLED);
        return orderRepository.save(order);
    }

    @Override
    public OrderItem findOrderItemById(Long id) throws Exception {
        return orderItemRepository.findById(id).orElseThrow(() -> new Exception("Order item does not exist"));
    }
}
