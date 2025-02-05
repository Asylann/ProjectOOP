package com.sportwearshop.sportwearwebshop.service;

import com.sportwearshop.sportwearwebshop.dto.OrderDetailsDTO;
import com.sportwearshop.sportwearwebshop.entity.Order;
import com.sportwearshop.sportwearwebshop.entity.OrderItem;
import com.sportwearshop.sportwearwebshop.entity.Product;
import com.sportwearshop.sportwearwebshop.entity.User;
import com.sportwearshop.sportwearwebshop.factory.OrderDTOFactory;
import com.sportwearshop.sportwearwebshop.repository.OrderItemRepository;
import com.sportwearshop.sportwearwebshop.repository.OrderRepository;
import com.sportwearshop.sportwearwebshop.repository.ProductRepository;
import com.sportwearshop.sportwearwebshop.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderDTOFactory orderDTOFactory;

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

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<OrderItem> getAllOrderItems() {
        return orderItemRepository.findAll();
    }

    public List<Order> getOrdersByUser(int userId) {
        return orderRepository.findByUserId(userId);
    }

    public OrderItem addOrderItem(int orderId, int productId, Integer quantity) {
        Order order = getOrder(orderId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Check if the item already exists in the order
        Optional<OrderItem> existingItem = orderItemRepository.findByOrderIdAndProductId(orderId, productId);
        OrderItem orderItem;

        if (existingItem.isPresent()) {
            // Update existing item
            orderItem = existingItem.get();
            orderItem.setQuantity(orderItem.getQuantity() + quantity);
            orderItem.setTotalAmount(orderItem.getTotalAmount() + (product.getPrice() * quantity));
        } else {
            orderItem = new OrderItem();
            orderItem.setOrderId(orderId);
            orderItem.setProductId(productId);
            orderItem.setQuantity(quantity);
            orderItem.setTotalAmount(product.getPrice() * quantity);
        }

        orderItemRepository.save(orderItem);

        updateOrderTotal(orderId);

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

    public OrderDetailsDTO getFullOrderDescription(int orderId) {
        Order order = orderRepository.findOrderWithItems(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        User user = userRepository.findById(order.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);

        List<Integer> productIds = orderItems.stream()
                .map(OrderItem::getProductId)
                .collect(Collectors.toList());

        List<Product> products = productRepository.findAllById(productIds);

        return orderDTOFactory.createOrderDetailsDTO(order, user, orderItems, products);
    }
}