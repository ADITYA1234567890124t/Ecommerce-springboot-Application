package com.ecommerce.controller;

import com.ecommerce.entity.Product;
import com.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    // ADMIN: Add product
    @PostMapping("/add")
    public Product addProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    // CUSTOMER: View all products
    @GetMapping("/all")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // CUSTOMER: Search by name
    @GetMapping("/search")
    public List<Product> search(@RequestParam String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }

    // CUSTOMER: Filter by category
    @GetMapping("/category/{category}")
    public List<Product> byCategory(@PathVariable String category) {
        return productRepository.findByCategory(category);
    }
}
