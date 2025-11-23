package com.learning.microservices.inventory_service.controller;


import com.learning.microservices.inventory_service.model.Product;
import com.learning.microservices.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    
    private final InventoryService inventoryService;
    
    @GetMapping
    public ResponseEntity<Collection<Product>> getAllProducts() {
        return ResponseEntity.ok(inventoryService.getAllProducts());
    }
    
    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable String productId) {
        Product product = inventoryService.getProduct(productId);
        if (product != null) {
            return ResponseEntity.ok(product);
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/{productId}/stock")
    public ResponseEntity<Product> updateStock(
            @PathVariable String productId, 
            @RequestParam int quantity) {
        Product product = inventoryService.addStock(productId, quantity);
        return ResponseEntity.ok(product);
    }
}