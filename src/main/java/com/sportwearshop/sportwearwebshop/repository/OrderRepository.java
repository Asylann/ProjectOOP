package com.sportwearshop.sportwearwebshop.repository;

import com.sportwearshop.sportwearwebshop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT o FROM Order o " +
            "LEFT JOIN FETCH OrderItem oi ON o.id = oi.orderId " +
            "WHERE o.id = :orderId")
    Optional<Order> findOrderWithItems(@Param("orderId") int orderId);

    List<Order> findByUserId(int userId);
}