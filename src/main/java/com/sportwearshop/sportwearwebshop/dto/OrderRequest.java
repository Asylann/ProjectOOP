package com.sportwearshop.sportwearwebshop.dto;

public class OrderRequest {
    private int userId;
    private String address;

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
