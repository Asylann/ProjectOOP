package com.sportwearshop.sportwearwebshop.service;

import com.sportwearshop.sportwearwebshop.entity.Order;
import com.sportwearshop.sportwearwebshop.entity.OrderItem;
import com.sportwearshop.sportwearwebshop.entity.Product;
import com.sportwearshop.sportwearwebshop.repository.OrderItemRepository;
import com.sportwearshop.sportwearwebshop.repository.OrderRepository;
import com.sportwearshop.sportwearwebshop.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    public Order createOrder(int userId, String address) {
        Order order = new Order();
        order.setUserId(userId);
        order.setAddress(address);
        order.setDate(LocalDate.now().toString());
        return orderRepository.save(order);
    }

    public Order getOrder(int id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public List<Order> getOrdersByUser(int userId) {
        return orderRepository.findByUserId(userId);
    }

    public OrderItem addOrderItem(int orderId, int productId, Integer quantity) {
        Order order = getOrder(orderId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(orderId);
        orderItem.setProductId(productId);
        orderItem.setQuantity(quantity);

        // Calculate total amount here instead of in the entity
        Double itemTotal = product.getPrice() * quantity;
        orderItem.setTotalAmount(itemTotal);

        orderItemRepository.save(orderItem);

        List<OrderItem> allItems = getOrderItems(orderId);
        allItems.add(orderItem);
        Double orderTotal = allItems.stream()
                .mapToDouble(OrderItem::getTotalAmount)
                .sum();
        order.setTotalAmount(orderTotal);

        orderRepository.save(order);

        return orderItem;
    }

    public List<OrderItem> getOrderItems(int orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }

    public void deleteOrderItem(int id) {
        OrderItem item = orderItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderItem not found"));

        Order order = getOrder(item.getOrderId());
        orderItemRepository.deleteById(id);

        List<OrderItem> remainingItems = getOrderItems(order.getId());
        Double orderTotal = remainingItems.stream()
                .mapToDouble(OrderItem::getTotalAmount)
                .sum();
        order.setTotalAmount(orderTotal);

        orderRepository.save(order);
    }

    public void deleteOrder(int id) {
        Order order = getOrder(id);

        List<OrderItem> items = orderItemRepository.findByOrderId(id);
        orderItemRepository.deleteAll(items);

        orderRepository.delete(order);
    }

    public Order updateOrder(int id, int userId, String address) {
        Order order = getOrder(id);
        order.setUserId(userId);
        order.setAddress(address);
        return orderRepository.save(order);
    }

    public OrderItem updateOrderItem(int id, int productId, Integer quantity) {
        OrderItem item = orderItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderItem not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        item.setProductId(productId);
        item.setQuantity(quantity);

        Double itemTotal = product.getPrice() * quantity;
        item.setTotalAmount(itemTotal);

        orderItemRepository.save(item);

        Order order = getOrder(item.getOrderId());
        List<OrderItem> allItems = getOrderItems(order.getId());
        Double orderTotal = allItems.stream()
                .mapToDouble(OrderItem::getTotalAmount)
                .sum();
        order.setTotalAmount(orderTotal);
        orderRepository.save(order);

        return item;
    }

    private void updateOrderTotal(int orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        List<OrderItem> items = orderItemRepository.findByOrderId(orderId);

        double total = items.stream()
                .mapToDouble(OrderItem::getTotalAmount)
                .sum();
        total = Math.round(total * 100.0) / 100.0;

        order.setTotalAmount(total);
        orderRepository.save(order);
    }
}
