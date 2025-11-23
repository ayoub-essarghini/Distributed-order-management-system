package com.learning.microservices.inventory_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryEvent implements Serializable {
    private String productId;
    private String orderId;
    private int quantity;
    private int currentStock;
    private String status;
    private LocalDateTime timestamp;
}