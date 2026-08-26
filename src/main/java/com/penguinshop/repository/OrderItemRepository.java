package com.penguinshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.penguinshop.domain.ORDER_STATUS;
import com.penguinshop.model.OrderItem;
import com.penguinshop.request.UserPurchaseStatus;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    // @Query("SELECT COUNT(oi) > 0 FROM OrderItem oi WHERE oi.userId = :userId AND oi.product.id = :productId AND oi.order.orderStatus = :status")
    // boolean hasUserPurchasedProduct(@Param("userId") Long userId, @Param("productId") Long productId,
    //         @Param("status") ORDER_STATUS status);

    @Query("SELECT new com.penguinshop.response.UserPurchaseStatus(" +
            "COUNT(oi) > 0, " +
            "COALESCE((SELECT COUNT(r) FROM Review r WHERE r.user.id = :userId AND r.product.id = :productId), 0)) " +
            "FROM OrderItem oi WHERE oi.userId = :userId AND oi.product.id = :productId AND oi.order.orderStatus = :status")
    UserPurchaseStatus getUserPurchaseStatus(
            @Param("userId") Long userId,
            @Param("productId") Long productId,
            @Param("status") ORDER_STATUS status);

    
}
