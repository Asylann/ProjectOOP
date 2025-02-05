package com.sportwearshop.sportwearwebshop.factory;

import com.sportwearshop.sportwearwebshop.dto.OrderDetailsDTO;
import com.sportwearshop.sportwearwebshop.dto.OrderItemDetailsDTO;
import com.sportwearshop.sportwearwebshop.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class OrderDTOFactory {

    private final OrderItemDetailsDTOFactory orderItemDetailsDTOFactory;

    public OrderDTOFactory(OrderItemDetailsDTOFactory orderItemDetailsDTOFactory) {
        this.orderItemDetailsDTOFactory = orderItemDetailsDTOFactory;
    }

    public OrderDetailsDTO createOrderDetailsDTO(Order order, User user, List<OrderItem> orderItems, List<Product> products) {
        OrderDetailsDTO dto = new OrderDetailsDTO();
        dto.setOrderId(order.getId());
        dto.setOrderDate(order.getDate());
        dto.setUserEmail(user.getEmail());
        dto.setShippingAddress(order.getAddress());
        dto.setTotalAmount(order.getTotalAmount());


        Map<Integer, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        List<OrderItemDetailsDTO> itemDTOs = orderItems.stream()
                .map(item -> {
                    Product product = productMap.get(item.getProductId());
                    if (product == null) {

                        throw new IllegalArgumentException("LOl product is not finded" + item.getProductId());

                    }
                    return orderItemDetailsDTOFactory.createOrderItemDetailsDTO(item, product);
                })
                .filter(itemDTO -> itemDTO!= null)
                .collect(Collectors.toList());

        dto.setItems(itemDTOs);
        return dto;
    }


    @Component
    public static class OrderItemDetailsDTOFactory {
        public OrderItemDetailsDTO createOrderItemDetailsDTO(OrderItem orderItem, Product product) {
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
}
