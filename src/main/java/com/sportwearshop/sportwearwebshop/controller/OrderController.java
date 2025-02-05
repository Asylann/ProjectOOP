package com.sportwearshop.sportwearwebshop.controller;

import com.sportwearshop.sportwearwebshop.dto.OrderDetailsDTO;
import com.sportwearshop.sportwearwebshop.dto.OrderRequest;
import com.sportwearshop.sportwearwebshop.entity.Order;
import com.sportwearshop.sportwearwebshop.entity.Product;
import com.sportwearshop.sportwearwebshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest request) {
        Order order = orderService.createOrder(request.getUserId(), request.getAddress());
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable int id) {
        Order order = orderService.getOrder(id);
        return ResponseEntity.ok(order);
    }
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getUserOrders(@PathVariable int userId) {
        List<Order> orders = orderService.getOrdersByUser(userId);
        return ResponseEntity.ok(orders);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable int id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable int id, @RequestBody OrderRequest request) {
        Order updatedOrder = orderService.updateOrder(id, request.getUserId(), request.getAddress());
        return ResponseEntity.ok(updatedOrder);
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<OrderDetailsDTO> getFullOrderDescription(@PathVariable int id) {
        OrderDetailsDTO orderDetails = orderService.getFullOrderDescription(id);
        return ResponseEntity.ok(orderDetails);
    }
}
