package com.sportwearshop.sportwearwebshop.dto;

import java.util.List;

public class OrderDetailsDTO {
    private int orderId;
    private String orderDate;
    private String userEmail;
    private String shippingAddress;
    private Double totalAmount;
    private List<OrderItemDetailsDTO> items;

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public String getOrderDate() { return orderDate; }
    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public List<OrderItemDetailsDTO> getItems() { return items; }
    public void setItems(List<OrderItemDetailsDTO> items) { this.items = items; }
}
