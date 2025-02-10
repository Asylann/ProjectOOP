package com.sportwearshop.sportwearwebshop.service;

import com.sportwearshop.sportwearwebshop.dto.CartDTO;
import com.sportwearshop.sportwearwebshop.dto.CartItemDTO;
import com.sportwearshop.sportwearwebshop.entity.Cart;
import com.sportwearshop.sportwearwebshop.entity.CartItem;
import com.sportwearshop.sportwearwebshop.entity.Product;
import com.sportwearshop.sportwearwebshop.repository.CartRepository;
import com.sportwearshop.sportwearwebshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public CartDTO getCartForUser(int userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> createNewCart(userId));
        return convertToDTO(cart);
    }

    @Transactional
    public CartDTO addItemToCart(int userId, int productId, int quantity) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> createNewCart(userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Check if product already exists in cart
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId() == productId)
                .findFirst();

        if (existingItem.isPresent()) {
            // Update existing item quantity
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            // Add new item
            CartItem newItem = new CartItem();
            newItem.setProductId(productId);
            newItem.setQuantity(quantity);
            cart.getItems().add(newItem);
        }

        cart = cartRepository.save(cart);
        return convertToDTO(cart);
    }

    @Transactional
    public CartDTO updateCartItemQuantity(int userId, int productId, int quantity) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getItems().stream()
                .filter(item -> item.getProductId() == productId)
                .findFirst()
                .ifPresent(item -> item.setQuantity(quantity));

        cart = cartRepository.save(cart);
        return convertToDTO(cart);
    }

    @Transactional
    public CartDTO removeItemFromCart(int userId, int productId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getItems().removeIf(item -> item.getProductId() == productId);
        cart = cartRepository.save(cart);
        return convertToDTO(cart);
    }

    @Transactional
    public void clearCart(int userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getItems().clear();
        cartRepository.save(cart);
    }

    private Cart createNewCart(int userId) {
        Cart cart = new Cart(userId);
        return cartRepository.save(cart);
    }

    private CartDTO convertToDTO(Cart cart) {
        CartDTO dto = new CartDTO();
        dto.setUserId(cart.getUserId());

        List<CartItemDTO> itemDTOs = new ArrayList<>();
        double totalAmount = 0.0;

        for (CartItem item : cart.getItems()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            CartItemDTO itemDTO = new CartItemDTO();
            itemDTO.setProductId(item.getProductId());
            itemDTO.setProductName(product.getName());
            itemDTO.setPrice(product.getPrice());
            itemDTO.setQuantity(item.getQuantity());
            itemDTO.setSubtotal(product.getPrice() * item.getQuantity());

            itemDTOs.add(itemDTO);
            totalAmount += itemDTO.getSubtotal();
        }

        dto.setItems(itemDTOs);
        dto.setTotalAmount(totalAmount);
        return dto;
    }
}
