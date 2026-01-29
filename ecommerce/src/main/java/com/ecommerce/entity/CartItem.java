package com.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // MANY ITEMS -> ONE CART
    @ManyToOne
    @JoinColumn(name = "cart_id")
    @JsonIgnoreProperties("user")
    private Cart cart;

    // MANY ITEMS -> ONE PRODUCT
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;
}