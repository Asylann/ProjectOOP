package com.sportwearshop.sportwearwebshop.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer stock;

    @Column
    private String size;

    public Product() {};

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public static class Builder {
        private Product product;

        public Builder() {
            product = new Product();
        }

        public Builder name(String name) {
            product.name = name;
            return this;
        }

        public Builder description(String description) {
            product.description = description;
            return this;
        }

        public Builder category(Category category) {
            product.category = category;
            return this;
        }

        public Builder price(Double price) {
            product.price = price;
            return this;
        }

        public Builder stock(Integer stock) {
            product.stock = stock;
            return this;
        }

        public Builder size(String size) {
            product.size = size;
            return this;
        }

        public Product build() {
            if (product.name == null || product.name.trim().isEmpty()) {
                throw new IllegalStateException("Product name is required");
            }
            if (product.category == null) {
                throw new IllegalStateException("Product category is required");
            }
            if (product.price == null || product.price <= 0) {
                throw new IllegalStateException("Valid product price is required");
            }
            if (product.stock == null || product.stock < 0) {
                throw new IllegalStateException("Valid product stock is required");
            }

            return product;
        }
    }
}


