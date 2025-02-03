package com.sportwearshop.sportwearwebshop.dto;

public class OrderItemRequest {
    private int orderId;
    private int productId;
    private Integer quantity;

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}