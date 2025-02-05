package com.sportwearshop.sportwearwebshop.controller;

import com.sportwearshop.sportwearwebshop.dto.OrderRequest;
import com.sportwearshop.sportwearwebshop.dto.ProductDTO;
import com.sportwearshop.sportwearwebshop.entity.Order;
import com.sportwearshop.sportwearwebshop.entity.Product;
import com.sportwearshop.sportwearwebshop.repository.ProductRepository;
import com.sportwearshop.sportwearwebshop.service.OrderService;
import com.sportwearshop.sportwearwebshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductService productService;

    public ProductController(ProductRepository productRepository, ProductService productService) {
        this.productRepository = productRepository;
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/category/{categoryId}")
    public List<Product> getProductsByCategory(@PathVariable int categoryId) {
        return productRepository.findByCategory_Id(categoryId);
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable int id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable int id, @RequestBody Product updatedProduct) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(updatedProduct.getName());
                    product.setDescription(updatedProduct.getDescription());
                    product.setPrice(updatedProduct.getPrice());
                    product.setStock(updatedProduct.getStock());
                    product.setCategory(updatedProduct.getCategory());
                    product.setSize(updatedProduct.getSize());
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new RuntimeException("Product not found with id " + id));
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable int id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
    }

    @PostMapping
    public Product createProduct(@RequestBody ProductDTO request){
        Product product = productService.createProductByBuilder(request);
        return product;
    }
}


