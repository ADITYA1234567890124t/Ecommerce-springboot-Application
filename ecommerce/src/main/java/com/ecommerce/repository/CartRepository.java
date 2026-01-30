package com.ecommerce.repository;

import com.ecommerce.entity.Cart;
import com.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    // ✅ FOR CART CONTROLLER
    Optional<Cart> findByUser(User user);

    // ✅ FOR ORDER SERVICE
    List<Cart> findByUserId(Long userId);
}