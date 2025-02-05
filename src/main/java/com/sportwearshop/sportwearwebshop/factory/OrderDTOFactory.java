package com.sportwearshop.sportwearwebshop.factory;

import com.sportwearshop.sportwearwebshop.dto.OrderDetailsDTO;
import com.sportwearshop.sportwearwebshop.dto.OrderItemDetailsDTO;
import com.sportwearshop.sportwearwebshop.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderDTOFactory {

    public OrderDetailsDTO createOrderDetailsDTO(Order order, User user, List<OrderItem> orderItems, List<Product> products) {
        OrderDetailsDTO dto = new OrderDetailsDTO();
        dto.setOrderId(order.getId());
        dto.setOrderDate(order.getDate());
        dto.setUserEmail(user.getEmail());
        dto.setShippingAddress(order.getAddress());
        dto.setTotalAmount(order.getTotalAmount());

        List<OrderItemDetailsDTO> itemDTOs = orderItems.stream()
                .map(item -> createOrderItemDetailsDTO(item,
                        products.stream()
                                .filter(p -> p.getId() == item.getProductId())
                                .findFirst()
                                .orElse(null)))
                .collect(Collectors.toList());

        dto.setItems(itemDTOs);
        return dto;
    }

    private OrderItemDetailsDTO createOrderItemDetailsDTO(OrderItem orderItem, Product product) {
        OrderItemDetailsDTO dto = new OrderItemDetailsDTO();
        dto.setProductName(product.getName());
        dto.setProductCategory(product.getCategory().getName());
        dto.setSize(product.getSize());
        dto.setQuantity(orderItem.getQuantity());
        dto.setPrice(product.getPrice());
        dto.setTotalAmount(orderItem.getTotalAmount());
        return dto;
    }
}