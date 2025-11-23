package com.learning.microservices.inventory_service.service;

import com.learning.microservices.inventory_service.config.RabbitMQConfig;
import com.learning.microservices.inventory_service.model.InventoryEvent;
import com.learning.microservices.inventory_service.model.OrderEvent;
import com.learning.microservices.inventory_service.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final RabbitTemplate rabbitTemplate;
    private final ConcurrentHashMap<String, Product> inventory = new ConcurrentHashMap<>();


    public InventoryService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        // Initialize products
        inventory.put("PROD-001", new Product("PROD-001", "Laptop", 50));
        inventory.put("PROD-002", new Product("PROD-002", "Mouse", 200));
        inventory.put("PROD-003", new Product("PROD-003", "Keyboard", 150));
        System.out.println("Inventory initialized with " + inventory.size() + " products");

    }

    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE)
    public void handleOrderCreated(OrderEvent orderEvent) {
        System.out.println("Received order event " + orderEvent.getOrderId());

        Product product = inventory.get(orderEvent.getOrderId());
        InventoryEvent inventoryEvent;
        if (product != null && product.getStock() >= orderEvent.getQuantity()) {
            product.setStock(product.getStock() - orderEvent.getQuantity());
            inventory.put(orderEvent.getOrderId(), product);

            inventoryEvent = new InventoryEvent(orderEvent.getProductId(), orderEvent.getOrderId(), orderEvent.getQuantity(), product.getStock(), "RESERVED", LocalDateTime.now());
            System.out.println("Stock reserved for order: " + orderEvent.getOrderId());

        } else {
            inventoryEvent = new InventoryEvent(orderEvent.getProductId(), orderEvent.getOrderId(), orderEvent.getQuantity(), product != null ? product.getStock() : 0, "INSUFFICIENT_STOCK", LocalDateTime.now());
            System.out.println(" Insufficient stock for order: " + orderEvent.getOrderId());

        }
        rabbitTemplate.convertAndSend(RabbitMQConfig.INVENTORY_QUEUE, inventoryEvent);
    }

    public Product getProduct(String productId) {
        return inventory.get(productId);
    }

    public Collection<Product> getAllProducts() {
        return inventory.values();
    }

    public Product addStock(String productId, int quantity) {
        Product product = inventory.get(productId);
        if (product != null) {
            product.setStock(product.getStock() + quantity);
            inventory.put(productId, product);
        }
        return product;
    }


}
