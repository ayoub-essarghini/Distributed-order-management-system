package com.learning.microservices.notification_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent {
    private String orderId;
    private String customerId;
    private String productId;
    private int quantity;
    private double totalPrice;
    private String eventType;
    private LocalDateTime timestamp;
}