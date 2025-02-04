package com.sportwearshop.sportwearwebshop.repository;

import com.sportwearshop.sportwearwebshop.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    List<OrderItem> findByOrderId(int orderId);
    Optional<OrderItem> findByOrderIdAndProductId(int orderId, int productId);

}
