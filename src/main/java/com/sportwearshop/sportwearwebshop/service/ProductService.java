package com.sportwearshop.sportwearwebshop.service;


import com.sportwearshop.sportwearwebshop.entity.Product;
import com.sportwearshop.sportwearwebshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository){

        this.productRepository = productRepository;
    }

    public List<Product> getProducts() {

        return (List<Product>) productRepository.findAll();
    }
    public Product getProductById(int id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            return product.get();
        } else {
            throw new RuntimeException("Product not found with ID: " + id);
        }
    }
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }
    public void createNewProduct(Product product) {
        List<Product> existingProducts = productRepository.findByName(product.getName());
        if (!existingProducts.isEmpty()) {
            throw new IllegalStateException("Product name already taken");
        }
        productRepository.save(product);
    }

    public String checkStock(int productId) {
        Optional<Product> product = productRepository.findById(productId);

        if (product.isPresent()) {
            if (product.get().getStock() == 0) {
                return "We do not have such a product yet.";
            } else {
                return "Stock available for " + product.get().getName() + ": " + product.get().getStock();
            }
        } else {
            throw new RuntimeException("Product not found with ID: " + productId);
        }
    }
}
