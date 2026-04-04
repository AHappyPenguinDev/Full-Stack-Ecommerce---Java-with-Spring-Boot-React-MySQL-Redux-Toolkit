package com.penguinshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.penguinshop.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{

}
