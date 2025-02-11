package com.sportwearshop.sportwearwebshop.controller;

import com.sportwearshop.sportwearwebshop.entity.Cart;
import com.sportwearshop.sportwearwebshop.dto.AddToCartRequest;
import com.sportwearshop.sportwearwebshop.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable int userId) {
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @PostMapping("/{userId}/add")
    public ResponseEntity<Cart> addToCart(@PathVariable int userId, @RequestBody AddToCartRequest request) {
        return ResponseEntity.ok(cartService.addToCart(userId, request));
    }

    @DeleteMapping("/{userId}/items/{productId}")
    public ResponseEntity<Void> removeItem(@PathVariable int userId, @PathVariable int productId) {
        cartService.removeItemFromCart(userId, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable int userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}
