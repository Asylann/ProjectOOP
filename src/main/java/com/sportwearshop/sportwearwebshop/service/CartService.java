package com.sportwearshop.sportwearwebshop.service;

import com.sportwearshop.sportwearwebshop.entity.Cart;
import com.sportwearshop.sportwearwebshop.entity.CartItem;
import com.sportwearshop.sportwearwebshop.repository.CartItemRepository;
import com.sportwearshop.sportwearwebshop.repository.CartRepository;
import com.sportwearshop.sportwearwebshop.dto.AddToCartRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public Cart getCartByUserId(int userId) {
        return cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart newCart = new Cart(userId);
            return cartRepository.save(newCart);
        });
    }

    @Transactional
    public Cart addToCart(int userId, AddToCartRequest request) {
        Cart cart = getCartByUserId(userId);
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId() == request.getProductId())
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + request.getQuantity());
        } else {
            CartItem newItem = new CartItem(cart, request.getProductId(), request.getQuantity());
            cart.getItems().add(newItem);
        }

        return cartRepository.save(cart);
    }

    @Transactional
    public void removeItemFromCart(int userId, int productId) {
        Cart cart = getCartByUserId(userId);
        cart.getItems().removeIf(item -> item.getProductId() == productId);
        cartRepository.save(cart);
    }

    @Transactional
    public void clearCart(int userId) {
        Cart cart = getCartByUserId(userId);
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
