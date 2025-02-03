package com.sportwearshop.sportwearwebshop.controller;

import com.sportwearshop.sportwearwebshop.dto.OrderItemRequest;
import com.sportwearshop.sportwearwebshop.entity.OrderItem;
import com.sportwearshop.sportwearwebshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderItem> addOrderItem(@RequestBody OrderItemRequest request) {
        OrderItem orderItem = orderService.addOrderItem(
                request.getOrderId(),
                request.getProductId(),
                request.getQuantity()
        );
        return new ResponseEntity<>(orderItem, HttpStatus.CREATED);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderItem>> getOrderItems(@PathVariable int orderId) {
        List<OrderItem> items = orderService.getOrderItems(orderId);
        return ResponseEntity.ok(items);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable int id) {
        orderService.deleteOrderItem(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderItem> updateOrderItem(@PathVariable int id, @RequestBody OrderItemRequest request) {
        OrderItem updatedItem = orderService.updateOrderItem(id, request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(updatedItem);
    }
}
