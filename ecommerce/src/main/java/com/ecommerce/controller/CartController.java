package com.ecommerce.controller;

import com.ecommerce.entity.Cart;
import com.ecommerce.entity.CartItem;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.repository.CartItemRepository;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@CrossOrigin(origins = "http://localhost:3000")
public class CartController {

    @Autowired
    private CartRepository cartRepo;

    @Autowired
    private CartItemRepository cartItemRepo;

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private UserRepository userRepo;

    // ✅ 1. GET CART BY USER
    @GetMapping("/user/{userId}")
    public List<CartItem> getCartByUser(@PathVariable Long userId) {

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepo.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        return cartItemRepo.findByCart(cart);
    }

    // ✅ 2. ADD TO CART (MERGE IF EXISTS)
    @PostMapping("/add/{userId}/{productId}")
    public CartItem addToCart(
            @PathVariable Long userId,
            @PathVariable Long productId,
            @RequestParam int qty) {

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Cart cart = cartRepo.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepo.save(newCart);
                });

        // 🔥 Check if product already exists in cart
        List<CartItem> items = cartItemRepo.findByCart(cart);
        for (CartItem item : items) {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(item.getQuantity() + qty);
                return cartItemRepo.save(item);
            }
        }

        // 🔥 Create new cart item
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(qty);

        return cartItemRepo.save(cartItem);
    }

    // ✅ 3. REMOVE ITEM
    @DeleteMapping("/item/{itemId}")
    public void removeItem(@PathVariable Long itemId) {
        cartItemRepo.deleteById(itemId);
    }

    // ✅ 4. UPDATE QUANTITY
    @PutMapping("/item/{itemId}")
    public CartItem updateQuantity(
            @PathVariable Long itemId,
            @RequestParam int qty) {

        CartItem item = cartItemRepo.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        item.setQuantity(qty);
        return cartItemRepo.save(item);
    }
}
