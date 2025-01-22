package com.sportwearshop.sportwearwebshop.service;


import com.sportwearshop.sportwearwebshop.entity.Product;
import com.sportwearshop.sportwearwebshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private ProductRepository productRepository;


    @Autowired
    public ProductService(ProductRepository pruductRepository){
        this.productRepository = pruductRepository;
    }

    public List<Product> getProducts() {
        return (List<Product>) productRepository.findAll();
    }
}
