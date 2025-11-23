package com.learning.microservices.notification_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryEvent {
    private String productId;
    private String orderId;
    private int quantity;
    private int currentStock;
    private String status;
    private LocalDateTime timestamp;
}

